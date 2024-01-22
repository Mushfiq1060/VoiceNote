package com.openai.voicenote.core.data.wearDataLayer

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.openai.voicenote.core.common.di.ApplicationScope
import com.openai.voicenote.core.common.utils.Utils.fromJson
import com.openai.voicenote.core.common.utils.Utils.toJson
import com.openai.voicenote.core.data.local.NoteDataSource
import com.openai.voicenote.core.model.NoteResource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.URLDecoder
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class DataLayer @Inject constructor(
    @ApplicationContext context: Context,
    private val noteDataSource: NoteDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) {

    private val capabilityClient by lazy { Wearable.getCapabilityClient(context) }
    private val dataClient by lazy { Wearable.getDataClient(context) }
    private val messageClient by lazy { Wearable.getMessageClient(context) }

    private val capabilityClientListener = CapabilityClient.OnCapabilityChangedListener {
        Log.i(TAG, "Capability client -> $it")
    }

    private val dataClientListener = DataClient.OnDataChangedListener { dataEvents ->
        dataEvents.map { dataEvent ->
            when (dataEvent.type) {
                DataEvent.TYPE_CHANGED -> {
                    when (dataEvent.dataItem.uri.path)  {
                        NOTE_PATH -> {
                            val noteString = DataMapItem.fromDataItem(dataEvent.dataItem)
                                .dataMap.getString(NOTE)
                            if (noteString != null) {
                                val decodeNoteString = URLDecoder.decode(noteString, "UTF-8")
                                val note = decodeNoteString.fromJson(NoteResource::class.java)
                                scope.launch {
                                    noteDataSource.insertNote(listOf(note))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private val messageClientListener = MessageClient.OnMessageReceivedListener { messageEvent ->
        scope.launch {
            val noteList: List<NoteResource> = when (messageEvent.path) {
                PIN_NOTES_PATH -> {
                    noteDataSource.observeAllPinNotes().first()
                }
                OTHER_NOTES_PATH -> {
                    noteDataSource.observeAllOtherNotes().first()
                }
                ARCHIVE_NOTES_PATH -> { listOf() }
                TRASH_NOTES_PATH -> { listOf() }
                else -> { listOf() }
            }
            val list = noteList.map { it.toJson() }
            val request = PutDataMapRequest.create(NOTES_PATH).apply {
                dataMap.putLong(TIME, System.currentTimeMillis())
                dataMap.putStringArray(NOTES, list.toTypedArray())
            }
                .asPutDataRequest()
                .setUrgent()
            dataClient.putDataItem(request).await()
        }
    }

    suspend fun addListener() {
        capabilityClient.addListener(
            capabilityClientListener,
            Uri.parse("wear://"),
            CapabilityClient.FILTER_REACHABLE
        )
        try {
            capabilityClient.addLocalCapability(TEXT_CAPABILITY).await()
            Log.i(TAG, "Add capability successfully")
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            Log.e(TAG, "Could not add capability $exception")
        }
        dataClient.addListener(dataClientListener)
        messageClient.addListener(messageClientListener)
    }

    suspend fun removeListener() {
        capabilityClient.removeListener(capabilityClientListener)
        dataClient.removeListener(dataClientListener)
        messageClient.removeListener(messageClientListener)
        withContext(NonCancellable) {
            try {
                capabilityClient.removeLocalCapability(TEXT_CAPABILITY).await()
                Log.i(TAG, "Remove capability successfully")
            } catch (exception: Exception) {
                Log.e(TAG, "Could not remove capability $exception")
            }
        }
    }

    companion object {
        const val TAG = "DATA LAYER MOBILE"
        const val TEXT_CAPABILITY = "text"
        const val PIN_NOTES_PATH = "/pinned-notes-path"
        const val OTHER_NOTES_PATH = "/other-notes-path"
        const val ARCHIVE_NOTES_PATH = "/archive-notes-path"
        const val TRASH_NOTES_PATH = "/trash-notes-path"
        const val NOTES_PATH = "/notes"
        const val NOTES = "notes"
        const val TIME = "time"
        private const val NOTE_PATH = "/note-path"
        private const val NOTE = "/note"
    }

}