package com.planner.services

import android.util.Log
import com.planner.misc.Properties
import com.planner.models.SSEEvent
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit

class SSEConnection {
    private val EVENTS_URL = "${Properties.apiUrl}/connect"
    var sseEventsFlow = MutableStateFlow(SSEEvent())

    private val sseClient = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .build()

    private val sseRequest = Request.Builder()
        .url(EVENTS_URL)
        .header("Accept", "application/json")
        .addHeader("Accept", "text/event-stream")
        .build()

    private var sseEventSourceListener = object : EventSourceListener() {
        override fun onOpen(eventSource: EventSource, response: Response) {
            Log.d("TEST_SSE", "REPO| Conexão aberta")

            val event = SSEEvent("open")
            sseEventsFlow.tryEmit(event)
        }

        override fun onClosed(eventSource: EventSource) {
            Log.d("TEST_SSE", "REPO| Conexão fechada")

            val event = SSEEvent("closed")
            sseEventsFlow.tryEmit(event)
        }

        override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
            Log.d("TEST_SSE", "REPO| Evento, Tipo: $type | Data: $data")

            val event = SSEEvent(type ?: "", data)
            sseEventsFlow.tryEmit(event)
        }

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
            Log.d("TEST_SSE", "REPO| Erro:")
            Log.d("TEST_SSE", "$response")
            t?.printStackTrace()

            val event = SSEEvent("error")
            sseEventsFlow.tryEmit(event)
        }
    }

    init {
        initEventSource()
    }

    private fun initEventSource() {
        EventSources.createFactory(sseClient).newEventSource(sseRequest, sseEventSourceListener)
    }
}