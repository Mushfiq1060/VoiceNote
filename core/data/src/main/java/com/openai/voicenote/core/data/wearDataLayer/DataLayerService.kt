package com.openai.voicenote.core.data.wearDataLayer

import android.content.Context
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.openai.voicenote.core.common.di.ApplicationScope
import com.openai.voicenote.core.common.utils.Utils.fromJson
import com.openai.voicenote.core.common.utils.Utils.toJson
import com.openai.voicenote.core.data.local.NoteDataSource
import com.openai.voicenote.core.model.NoteResource
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.net.URLDecoder
import javax.inject.Inject

@AndroidEntryPoint
class DataLayerService : WearableListenerService() {

    @Inject lateinit var noteDataSource: NoteDataSource
    @Inject @ApplicationContext lateinit var context: Context
    @Inject @ApplicationScope lateinit var scope: CoroutineScope

    private val dataClient by lazy { Wearable.getDataClient(context) }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
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

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
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

    companion object {
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