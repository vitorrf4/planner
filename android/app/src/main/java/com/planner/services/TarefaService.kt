package com.planner.services

import com.planner.models.Tarefa
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TarefaService {
    @POST("adicionar")
    fun addTarefa(@Body tarefa: Tarefa) : Call<String>
    @POST("replace")
    fun replaceTarefas(@Body tarefas: List<Tarefa>) : Call<String>
}