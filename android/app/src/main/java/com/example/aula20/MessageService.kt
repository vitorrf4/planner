package com.example.aula20

import com.example.aula20.models.Tarefa
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TarefaService {
    @POST("sse/add-tarefa")
    fun addTarefa(@Body tarefa: Tarefa) : Call<Tarefa>
    @POST("tarefas/replace")
    fun replaceTarefas(@Body tarefas: List<Tarefa>) : Call<String>
}