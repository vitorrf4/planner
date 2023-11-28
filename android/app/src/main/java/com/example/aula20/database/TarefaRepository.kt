package com.example.aula20.database

import android.content.Context
import com.example.aula20.models.Tarefa

class TarefaRepository(context: Context) {
    private val DAO = TarefaDataBase.getInstance(context).getDAO()

    fun salvarTarefa(tarefa: Tarefa) : Boolean {
        return DAO.salvarTarefa(tarefa) > 0
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