package com.planner.services

import com.planner.models.Tarefa
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface TarefaService {
    @POST("adicionar")
    fun addTarefa(@Body tarefa: Tarefa) : Call<String>
    @POST("replace")
    fun replaceTarefas(@Body tarefas: List<Tarefa>) : Call<String>
    @DELETE("deletar/{id}")
    fun excluirTarefa(@Path(value = "id") id: Int) : Call<String>
}