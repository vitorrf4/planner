package com.planner.services

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.planner.models.EVENT_STATUS
import com.planner.models.SSEEventData
import com.planner.models.Tarefa
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class SSEConnection {
    private val EVENTS_URL = "${com.planner.Properties.apiUrl}/sse/connect"

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

    // flow
    var sseEventsFlow = MutableStateFlow(SSEEventData(EVENT_STATUS.NONE))

    private val sseEventSourceListener = object : EventSourceListener() {
        override fun onOpen(eventSource: EventSource, response: Response) {
            super.onOpen(eventSource, response)

            Log.d("TEST_SSE", "REPO| Connection opened")
            val event = SSEEventData(EVENT_STATUS.OPEN)
            sseEventsFlow.tryEmit(event)
        }

        override fun onClosed(eventSource: EventSource) {
            super.onClosed(eventSource)

            Log.d("TEST_SSE", "REPO| Connection closed")
            val event = SSEEventData(EVENT_STATUS.CLOSED)
            sseEventsFlow.tryEmit(event)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
            super.onEvent(eventSource, id, type, data)

            Log.d("TEST_SSE", "REPO| event received, Data: $data")
            var event = SSEEventData(EVENT_STATUS.SUCCESS)

            if (data != "\"[]\"") {
                event.tarefa = parseJsonToTarefa(data)
            }

            sseEventsFlow.tryEmit(event)
        }

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
            super.onFailure(eventSource, t, response)

            Log.d("TEST_SSE", "REPO| Failure:")
            Log.d("TEST_SSE", "$response")
            Log.d("TEST_SSE", "${response?.headers}")
            t?.printStackTrace()

            val event = SSEEventData(EVENT_STATUS.ERROR)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseJsonToTarefa(data: String): Tarefa {
        val gson = Gson()
        val jsonObject = gson.fromJson(data, JsonObject::class.java)
        Log.d("TEST_SSE", "json object: $jsonObject")

        var titulo = jsonObject.get("titulo").asString
        var descricao = jsonObject.get("descricao").asString
        var tarefa = Tarefa(0, titulo, descricao, LocalDateTime.now())

        return tarefa
    }
}