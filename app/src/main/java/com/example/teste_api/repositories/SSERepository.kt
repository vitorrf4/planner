package com.example.teste_api.repositories

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit

class SSERepository {
    private val EVENTSURL = "https://every-swans-wish.loca.lt"
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

    private val sseEventSourceListener = object : EventSourceListener() {
        override fun onClosed(eventSource: EventSource) {}

        override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {}

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {}

        override fun onOpen(eventSource: EventSource, response: Response) {}
    }

    init {
        initEventSource()
    }

    private fun initEventSource() {
        EventSources.createFactory(sseClient)
            .newEventSource(request = sseRequest, listener = sseEventSourceListener)
    }



}