package com.example.aula20.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aula20.models.Tarefa
import com.example.aula20.models.ValidarTarefas
import com.example.aula20.database.TarefaRepository
import java.time.LocalDateTime

class CadastroViewModel(application: Application) : AndroidViewModel(application) {

    // dado "vivo" que será observado pela CadastroActivity:
    private var txtToast = MutableLiveData<String>()
    // objeto responsável por qualquer validação das tarefas:
    private var validacao = ValidarTarefas()
    // objeto responsável por acessar banco de dados:
    private var tarefaRepository = TarefaRepository(application.applicationContext)

    // objeto "vivo" que receberá uma tarefa do data base
    private var tarefaFromDB = MutableLiveData<Tarefa>()

    // método para retornar o parametro vivo "tarefaFromDB"
    fun getTarefaFromDB() : LiveData<Tarefa>{
        return tarefaFromDB
    }

    fun getTxtToast() : LiveData<String> {
        return txtToast
    }

    // método que irá receber uma string, verificar se ela não está em branco e,
    // caso não esteja, tentará salvar um objeto 'tarefa' no bacno de dados.
    // ela retornará 'false' se a string recebida estiver em branco ou o ROOM não
    // conseguir salvar o objeto no banco de dados.
    // E retornará 'true' se o ROOM conseguir salvar com sucesso o objeto no BD
    @RequiresApi(Build.VERSION_CODES.O)
    fun salvarTarefa(nomeTarefa: String, descricao: String, dataFinal: LocalDateTime) : Boolean {

        // passo 1: verificar se a string recebida está em branco
        if (validacao.verificarCampoVazio(nomeTarefa)){
            // se retornar true, a string recebida da CadastroActivity está vazia
            // nesse caso:
            txtToast.value = "Informe o nome da tarefa!"
            return false // não conseguimos salvar a tarefa no banco de dados
        }

        // passo 2: se a string não está em branco, então podemos
        // criar um novo objeto do tipo Tarefa
        var tarefa = Tarefa(0, nomeTarefa, descricao, dataFinal)

        // Passo 3: tentar salvar no banco de dados este objeto tarefa
        if (!tarefaRepository.salvarTarefa(tarefa)){
            // se o não conseguiu salvar:
            txtToast.value = "Erro ao tentar salvar tarefa. Tente novamente mais tarde"
            return false // não conseguimos salvar a tarefa no banco de dados
        }

        // se a função acima salvarTarefa retornou true:
        txtToast.value = "Tarefa cadastrada com sucesso!"
        return true // conseguimos salvar a tarefa com sucesso no banco de dados!

    }

    // método que recebe um id, procura uma tarefa no databa base que contenha
    // este exato id e, ao encontrar, seta a tarefa encontrada como valor do
    // parametro vivo "tarefaFromDB". Quem estiver obserando este atributo
    // receberá o valor vindo do banco
    fun findTarefa(id: Int) {
       tarefaFromDB.value = tarefaRepository.getTarefa(id)
    }

    // método que recebe uma tarefa e atualiza ela no banco de dados
    fun atualizarTarefa(tarefa: Tarefa) : Boolean {

        if (validacao.verificarCampoVazio(tarefa.titulo)){
            txtToast.value = "Informe o nome da tarefa"
            return false // não conseguiu atualizar a tarefa no banco
        }

        // se passar pelo guard clause acima, o método prossegue com o update
        tarefaRepository.atualizarTarefa(tarefa)
        txtToast.value = "Tarefa atualizada"
        return true // conseguiu atualizar a tarefa no banco
    }

}