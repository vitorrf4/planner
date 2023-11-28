package com.example.aula20.services.repository

import androidx.room.*
import com.example.aula20.services.model.Tarefa

@Dao
interface TarefaDAO {

    @Insert
    fun salvarTarefa(tarefa: Tarefa) : Long

    @Delete
    fun excluirTarefa(tarefa: Tarefa)

    @Update
    fun atualizarTarefa(tarefa: Tarefa)

    @Query("SELECT * FROM tarefas WHERE id = :id")
    fun getTarefa(id: Int) : Tarefa

    @Query("SELECT * FROM tarefas")
    fun getTarefas() : List<Tarefa>

}