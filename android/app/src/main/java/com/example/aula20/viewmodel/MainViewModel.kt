package com.example.aula20.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aula20.services.model.Tarefa
import com.example.aula20.services.repository.TarefaRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var listaTarefas = MutableLiveData<List<Tarefa>>()
    private var repository = TarefaRepository(application.applicationContext)
    private var txtToast = MutableLiveData<String>()

    fun getListaTarefas() : LiveData<List<Tarefa>> {
        return listaTarefas
    }

    fun getTxtToast() : LiveData<String> {
        return txtToast
    }


    // carregar as tarefas do data base e armazenar na nossa lista
    fun getTarefasFromDB(){
        listaTarefas.value = repository.getTarefas()
    }

    // excluir tarefa do data base
    fun excluirTarefa(tarefa: Tarefa) {
        repository.excluirTarefa(tarefa)
        txtToast.value = "Tarefa excluída"
    }

    fun salvarTarefa(tarefa: Tarefa) {
        // Passo 3: tentar salvar no banco de dados este objeto tarefa
        if (!repository.salvarTarefa(tarefa)) {
            // se o não conseguiu salvar:
            txtToast.value = "Erro ao tentar salvar tarefa. Tente novamente mais tarde"
        }
        listaTarefas.value = repository.getTarefas()
    }
}