package com.example.aula20

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.aula20.services.model.Tarefa
import com.example.teste_api.models.EVENT_STATUS
import com.example.teste_api.models.SSEEventData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class SSERepository {
    private val EVENTS_URL = "${Properties.apiUrl}/chat"

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

            //
//            var tarefas = parseJsonArrayToTarefas())
            //

            if (data != "[]") {
                Log.d("TEST_SSE", "if true")
                event.tarefa = parseJsonToTarefa(data)
            }

            Log.d("TEST_SSE", "tarefa titulo: ${data}")
//            Log.d("TEST_SSE", "tarefa desc: ${event.tarefa?.descricao}")
//            Log.d("TEST_SSE", "tarefa data: ${event.tarefa?.dataFinal}")


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
    fun parseJsonArrayToTarefas(data: String): List<Tarefa> {
        val listType = object : TypeToken<List<Map<String, String>>>() {}.type
        val jsonArray: List<Map<String, String>> = Gson().fromJson(data, listType)
        var tarefas = emptyList<Tarefa>()

        for (jsonObject in jsonArray) {
            var id = jsonObject.getValue("id")
            var titulo = jsonObject.getValue("titulo")
            var descricao = jsonObject.getValue("descricao")
            var tarefa = Tarefa(0, titulo, descricao, LocalDateTime.now())
//                var json = Gson().toJson(jsonObject)

//                tarefas = tarefas + Gson().fromJson(json, Tarefa::class.java)
            tarefas = tarefas + tarefa
            Log.d("TEST_SSE", "tarefa: $tarefa")
        }
        //

        return tarefas;
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