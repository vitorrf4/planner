package com.planner.database

import android.content.Context
import com.planner.models.Tarefa

class TarefaRepository(context: Context) {
    private val DAO = TarefaDataBase.getInstance(context).getDAO()

    fun salvarTarefa(tarefa: Tarefa) : Long {
        return DAO.salvarTarefa(tarefa)
    }

    fun excluirTarefa(tarefa: Tarefa){
        DAO.excluirTarefa(tarefa)
    }

    fun atualizarTarefa(tarefa: Tarefa) {
        DAO.atualizarTarefa(tarefa)
    }

    fun getTarefa(id: Int) : Tarefa {
        return DAO.getTarefa(id)
    }

    fun getTarefas(): List<Tarefa> {
        return DAO.getTarefas()
    }


}