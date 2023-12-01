package com.planner.services

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.planner.models.Tarefa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SSEService {
    @RequiresApi(Build.VERSION_CODES.O)
    private var retrofit = RetrofitClient.createMessageService()

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun excluirTarefa(id: Int) {
        var call = retrofit.excluirTarefa(id)

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun atualizarTarefa(tarefa: Tarefa) {
        var call = retrofit.atualizarTarefa(tarefa)

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