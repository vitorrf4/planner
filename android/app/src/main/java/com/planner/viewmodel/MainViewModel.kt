package com.planner.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.planner.database.TarefaRepository
import com.planner.models.SSEEvent
import com.planner.models.Tarefa
import com.planner.services.SSEService
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var listaTarefas = MutableLiveData<List<Tarefa>>()
    private var repository = TarefaRepository(application.applicationContext)
    private var txtToast = MutableLiveData<String>()
    private var service = SSEService()
    private val TAG = "TEST_SSE"

    fun getListaTarefas() : LiveData<List<Tarefa>> {
        return listaTarefas
    }

    fun getTxtToast() : LiveData<String> {
        return txtToast
    }

    fun getTarefasFromDB(){
        listaTarefas.value = repository.getTarefas()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun excluirTarefa(tarefa: Tarefa) {
        repository.excluirTarefa(tarefa)
        txtToast.value = "Tarefa excluída"

        getTarefasFromDB()

        service.excluirTarefa(tarefa.id);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun salvarTarefa(tarefa: Tarefa) {
        tarefa.id = repository.salvarTarefa(tarefa).toInt()

        if (tarefa.id <= 0) {
            txtToast.value = "Erro ao tentar salvar tarefa. Tente novamente mais tarde"
        }

        getTarefasFromDB()

        service.adicionarTarefa(tarefa)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun eventHandler(event: SSEEvent) {
        when (event.type) {
            "open" -> {
                Log.d(TAG, "VIEWMODEL| Event open, replacing tarefas...")

                service.replaceTarefas(repository.getTarefas())
            }

            "adicionar" -> {
                Log.d(TAG, "VIEWMODEL| Event adicionar")

                val tarefa = parseJsonToTarefa(event.data)
                salvarTarefa(tarefa)
            }

            "excluir" -> {
                Log.d(TAG, "VIEWMODEL| Event excluir")
                var id = getIdFromJson(event.data)
                var tarefa = repository.getTarefa(id)

                excluirTarefa(tarefa)
            }

            "error" -> {
                Log.d(TAG, "VIEWMODEL| Event error")
            }

            "closed" -> {
                Log.d(TAG, "VIEWMODEL| Connection closed")
            }

            else -> {}
        }
    }

    private fun getIdFromJson(data: String): Int {
        val gson = Gson()
        val id = gson.fromJson(data, Long::class.java)

        return id.toInt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseJsonToTarefa(data: String): Tarefa {
        val gson = Gson()
        val jsonObject = gson.fromJson(data, JsonObject::class.java)

        var titulo = jsonObject.get("titulo").asString
        var descricao = jsonObject.get("descricao").asString

        var dataFinal = LocalDateTime.now()

        try {
            val instant = Instant.parse(jsonObject.get("dataFinal").asString)

            val zoneId = ZoneId.of("America/Sao_Paulo")
            dataFinal = LocalDateTime.ofInstant(instant, zoneId)
        } catch (e: Exception) {
            Log.d(TAG, "VIEWMODEL| Error on parsing date")
        }

        return Tarefa(0, titulo, descricao, dataFinal)
    }
}