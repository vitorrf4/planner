package com.example.teste_api.repositories

import android.util.Log
import com.example.teste_api.models.SSEEventData
import com.example.teste_api.models.STATUS
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SSERepository {
    private val EVENTSURL = " https://spotty-animals-cheat.loca.lt/chat"

    private val sseClient = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .build()

    private val sseRequest = Request.Builder()
        .url(EVENTSURL)
        .header("Accept", "application/json")
        .addHeader("Accept", "text/event-stream")
        .build()


    // flow
    var sseEventsFlow = MutableStateFlow(SSEEventData(STATUS.NONE))

    private val sseEventSourceListener = object : EventSourceListener() {
        override fun onOpen(eventSource: EventSource, response: Response) {
            super.onOpen(eventSource, response)

            Log.d("TEST_SSE", "REPO| Connection opened")
            val event = SSEEventData(STATUS.OPEN)
            sseEventsFlow.tryEmit(event)
        }

        override fun onClosed(eventSource: EventSource) {
            super.onClosed(eventSource)
//            ---- do something ----

            Log.d("TEST_SSE", "REPO| Connection closed")
            val event = SSEEventData(STATUS.CLOSED)
            sseEventsFlow.tryEmit(event)
        }

        override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
            super.onEvent(eventSource, id, type, data)

//            ---- do something ----
            Log.d("TEST_SSE", "REPO| event received, Data: $data")

            var event = SSEEventData(STATUS.SUCCESS)
            event.message = data
            sseEventsFlow.tryEmit(event)
        }

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
            super.onFailure(eventSource, t, response)

            Log.d("TEST_SSE", "REPO| Failure:")
            Log.d("TEST_SSE", "${response}")
            Log.d("TEST_SSE", "${response?.headers}")
            t?.printStackTrace()

            val event = SSEEventData(STATUS.ERROR)
            sseEventsFlow.tryEmit(event)
        }

    }


    init {
        initEventSource()
    }
    private fun initEventSource() {
        EventSources.createFactory(sseClient)
            .newEventSource(request = sseRequest, listener = sseEventSourceListener)
    }


}