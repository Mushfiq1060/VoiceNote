package com.openai.voicenote.presentation.dataLayer

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityClient.OnCapabilityChangedListener
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageClient.OnMessageReceivedListener
import com.google.android.gms.wearable.Wearable
import com.openai.voicenote.presentation.model.NoteResource
import com.openai.voicenote.presentation.utils.Utils.fromJson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.URLDecoder
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class DataLayer @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val notesList = MutableStateFlow<List<NoteResource>>(listOf())
    private val loading = MutableStateFlow<Boolean>(true)

    private val capabilityClient by lazy { Wearable.getCapabilityClient(context) }
    private val dataClient by lazy { Wearable.getDataClient(context) }
    private val messageClient by lazy { Wearable.getMessageClient(context) }

    private val capabilityClientListener = OnCapabilityChangedListener {}

    private val dataClientListener = OnDataChangedListener { dataEvents ->
        dataEvents.map {  dataEvent ->
            when (dataEvent.type) {
                DataEvent.TYPE_CHANGED -> {
                    when (dataEvent.dataItem.uri.path) {
                        NOTES_PATH -> {
                            val notes = DataMapItem.fromDataItem(dataEvent.dataItem)
                                .dataMap.getStringArray(NOTES)
                            if (notes != null) {
                                val noteList = notes.toList().map {
                                    val note = URLDecoder.decode(it, "UTF-8")
                                    note.fromJson(NoteResource::class.java)
                                }
                                loading.update { false }
                                notesList.update { noteList }
                            }
                        }
                        else -> {}
                    }
                }
                else -> {}
            }
        }
    }

    private val messageClientListener = OnMessageReceivedListener {}

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

    fun getNotes(): Flow<List<NoteResource>> {
        return notesList.asStateFlow()
    }

    fun getLoadingStatus(): Flow<Boolean> {
        return loading.asStateFlow()
    }

    suspend fun sendMessageToHandHeldDevice(type: String) {
        notesList.update { listOf() }
        loading.update { true }
        try {
            val path = when (type) {
                "PIN_NOTES" -> PIN_NOTES_PATH
                "OTHER_NOTES" -> OTHER_NOTES_PATH
                "ARCHIVE_NOTES" -> ARCHIVE_NOTES_PATH
                "TRASH_NOTES" -> TRASH_NOTES_PATH
                else -> PIN_NOTES_PATH
            }
            val nodes = capabilityClient.getCapability(WEAR_CAPABILITY, CapabilityClient.FILTER_REACHABLE)
                .await().nodes
            nodes.map { node ->
                messageClient.sendMessage(node.id, path, byteArrayOf())
            }
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            Log.d(TAG, "Starting activity failed: $exception")
        }
    }

    companion object {
        private const val TAG = "WEAR APP DATA LAYER"
        private const val TEXT_CAPABILITY = "text"
        private const val PIN_NOTES_PATH = "/pinned-notes-path"
        private const val OTHER_NOTES_PATH = "/other-notes-path"
        private const val ARCHIVE_NOTES_PATH = "/archive-notes-path"
        private const val TRASH_NOTES_PATH = "/trash-notes-path"
        private const val NOTES_PATH = "/notes"
        private const val NOTES = "notes"
        private const val WEAR_CAPABILITY = "wear"

    }

}