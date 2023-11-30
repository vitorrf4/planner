package com.planner.database

import androidx.room.*
import com.planner.models.Tarefa

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