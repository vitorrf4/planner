package com.planner.services

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.planner.models.Tarefa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiService {
    @RequiresApi(Build.VERSION_CODES.O)
    private var retrofit = RetrofitClient.createMessageService()

    @RequiresApi(Build.VERSION_CODES.O)
    fun replaceTarefas(tarefas : List<Tarefa>) {
        var call = retrofit.replaceTarefas(tarefas)

        tratarResposta(call)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun adicionarTarefa(tarefa: Tarefa) {
        var call = retrofit.addTarefa(tarefa)

        tratarResposta(call)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deletarTarefa(id: Int) {
        var call = retrofit.deletarTarefa(id)

        tratarResposta(call)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun atualizarTarefa(tarefa: Tarefa) {
        var call = retrofit.atualizarTarefa(tarefa)

        tratarResposta(call)
    }

    private fun tratarResposta(call: Call<String>) {
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("TEST_SSE", "SSEService| Resposta: " +
                        "${response.code()} ${response.body()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                val error = t.message
                Log.d("TEST_SSE", "SSEService| Erro: $error")
            }
        })
    }
}