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

    private val capabilityClientListener = CapabilityClient.OnCapabilityChangedListener { capabilityInfo ->
        Log.i(DataLayerService.TAG, "Capability client -> $capabilityInfo")
    }

    private val dataClientListener = DataClient.OnDataChangedListener {}

    private val messageClientListener = MessageClient.OnMessageReceivedListener { messageEvent ->
        scope.launch {
            val noteList: List<NoteResource> = when (messageEvent.path) {
                DataLayerService.PIN_NOTES_PATH -> {
                    noteDataSource.observeAllPinNotes().first()
                }
                DataLayerService.OTHER_NOTES_PATH -> {
                    noteDataSource.observeAllOtherNotes().first()
                }
                DataLayerService.ARCHIVE_NOTES_PATH -> { listOf() }
                DataLayerService.TRASH_NOTES_PATH -> { listOf() }
                else -> { listOf() }
            }
            val list = noteList.map { it.toJson() }
            val request = PutDataMapRequest.create(DataLayerService.NOTES_PATH).apply {
                dataMap.putLong(DataLayerService.TIME, System.currentTimeMillis())
                dataMap.putStringArray(DataLayerService.NOTES, list.toTypedArray())
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
            capabilityClient.addLocalCapability(DataLayerService.TEXT_CAPABILITY).await()
            Log.i(DataLayerService.TAG, "Add capability successfully")
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            Log.e(DataLayerService.TAG, "Could not add capability $exception")
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
                capabilityClient.removeLocalCapability(DataLayerService.TEXT_CAPABILITY).await()
                Log.i(DataLayerService.TAG, "Remove capability successfully")
            } catch (exception: Exception) {
                Log.e(DataLayerService.TAG, "Could not remove capability $exception")
            }
        }
    }

}