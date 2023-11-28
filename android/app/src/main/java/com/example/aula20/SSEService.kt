package com.example.aula20

import RetrofitClient
import android.util.Log
import com.example.aula20.services.model.Tarefa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SSEService {
    fun replaceTarefas(tarefas : List<Tarefa>) {
        var service = RetrofitClient.createMessageService()
        var call = service.replaceTarefas(tarefas)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("TEST_SSE", "SSEService| Response success: ${response.body()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                val error = t.message
                Log.d("TEST_SSE", "SSEService| Error: $error")

            }
        })
    }
}