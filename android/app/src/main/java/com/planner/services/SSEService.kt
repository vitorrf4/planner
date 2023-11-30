package com.planner.services

import android.util.Log
import com.planner.models.Tarefa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SSEService {
    private var retrofit = RetrofitClient.createMessageService()

    fun replaceTarefas(tarefas : List<Tarefa>) {
        var call = retrofit.replaceTarefas(tarefas)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("TEST_SSE", "SSEService| Response: ${response.code()} ${response.body()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                val error = t.message
                Log.d("TEST_SSE", "SSEService| Error: $error")

            }
        })
    }

    fun adicionarTarefa(tarefa: Tarefa) {
        var call = retrofit.addTarefa(tarefa)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("TEST_SSE", "SSEService| Response: ${response.code()} ${response.body()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                val error = t.message
                Log.d("TEST_SSE", "SSEService| Error: $error")

            }
        })

    }
}