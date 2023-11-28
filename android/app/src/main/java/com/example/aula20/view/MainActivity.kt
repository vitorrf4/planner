package com.example.aula20.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aula20.SSEService
import com.example.aula20.SSEViewModel
import com.example.aula20.databinding.ActivityMainBinding
import com.example.aula20.models.EVENT_STATUS
import com.example.aula20.view.adapter.TarefaAdapter
import com.example.aula20.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TarefaAdapter
    private lateinit var viewModel : MainViewModel
    private lateinit var service : SSEService
    private val sseViewModel = SSEViewModel()
    private val TAG = "TEST_SSE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // inicializar adapter
        adapter = TarefaAdapter(this)
        // Inicializar o view model
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // setar o layour para a recycler view
        binding.rcvTarefas.layoutManager = LinearLayoutManager(this)
        sseViewModel.getSSEEvents()

        setObservers() // seta observadores para o view model
        setAdapter() // seta e configura adapter

        service = SSEService()

        binding.btnNovaTarefa.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        binding.btnRetry.setOnClickListener {
            if (sseViewModel.sseEvents.value?.eventStatus != EVENT_STATUS.CLOSED) {
                Toast.makeText(this, "Conexão já estabelecida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sseViewModel.retryConnection()
        }
    }

    fun setObservers(){
        viewModel.getListaTarefas().observe(this){
            adapter.updateTarefas(it)
        }
        viewModel.getTxtToast().observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        // observer da conexão com o Node
        sseViewModel.sseEvents.observe(this) {
            it?.let { event ->
                when(event.eventStatus) {
                    EVENT_STATUS.OPEN -> {
                        Log.d(TAG, "MAIN| Event open")
                    }

                    EVENT_STATUS.SUCCESS -> {
                        Log.d(TAG, "MAIN| Event successful")

                        if (event.tarefa == null) {
                            Log.d(TAG, "MAIN| replacing tarefas...")
                            service.replaceTarefas(viewModel.getListaTarefas().value ?: emptyList())
                            return@let
                        }

                        viewModel.salvarTarefa(event.tarefa!!)
                    }

                    EVENT_STATUS.ERROR -> {
                        Log.d(TAG, "MAIN| Event error")
                    }

                    EVENT_STATUS.CLOSED -> {
                        Log.d(TAG, "MAIN| Event closed")
                    }

                    else -> {}
                }
            }
        }
    }

    fun setAdapter(){
        // setar o adapter para a recycler view
        binding.rcvTarefas.adapter = adapter

        // setar uma função (anonima) para a variavel do adapter
        // responsavel pela ação de click longo (onItemLongClick)
        adapter.onItemLongClick = {
            var tarefaTemp = adapter.listaTarefas[it]
            viewModel.excluirTarefa(tarefaTemp)
            viewModel.getTarefasFromDB()
        }

        // setar uma função (anonima) para a variavel do adapter
        // responsavel pela ação de click simples (onItemClick)
        adapter.onItemClick = {
            var tarefaTemp = adapter.listaTarefas[it]
            var intent = Intent(this, CadastroActivity::class.java)
            intent.putExtra("idTarefa", tarefaTemp.id)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        // solicita ao view model que puxe os dados do banco de dados
        // para serem usados pelo adapter
        viewModel.getTarefasFromDB()
    }
}