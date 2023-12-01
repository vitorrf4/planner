package com.planner.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.planner.database.TarefaRepository
import com.planner.models.Tarefa
import com.planner.models.ValidarTarefas
import com.planner.services.SSEService
import java.time.LocalDateTime

class CadastroViewModel(application: Application) : AndroidViewModel(application) {
    private var txtToast = MutableLiveData<String>()
    private var validacao = ValidarTarefas()
    private var tarefaRepository = TarefaRepository(application.applicationContext)
    private var tarefaFromDB = MutableLiveData<Tarefa>()
    private var sseService =  SSEService()

    fun getTarefaFromDB() : LiveData<Tarefa> {
        return tarefaFromDB
    }

    fun getTxtToast() : LiveData<String> {
        return txtToast
    }

    fun findTarefa(id: Int) {
        tarefaFromDB.value = tarefaRepository.getTarefa(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun salvarTarefa(nomeTarefa: String, descricao: String, dataFinal: LocalDateTime) : Tarefa? {
        if (validacao.verificarCampoVazio(nomeTarefa)){
            txtToast.value = "Informe o nome da tarefa!"

            return null
        }

        var tarefa = Tarefa(0, nomeTarefa, descricao, dataFinal)

        tarefa.id = tarefaRepository.salvarTarefa(tarefa).toInt()
        if (tarefa.id <= 0) {
            txtToast.value = "Erro ao tentar salvar tarefa. Tente novamente mais tarde"
            return null
        }

        sseService.adicionarTarefa(tarefa)

        txtToast.value = "Tarefa cadastrada com sucesso!"
        return tarefa
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun atualizarTarefa(tarefa: Tarefa) : Boolean {
        if (validacao.verificarCampoVazio(tarefa.titulo)){
            txtToast.value = "Informe o nome da tarefa"
            return false
        }

        tarefaRepository.atualizarTarefa(tarefa)
        txtToast.value = "Tarefa atualizada"

        sseService.atualizarTarefa(tarefa)

        return true
    }

}