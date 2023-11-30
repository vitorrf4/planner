package com.planner.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.planner.databinding.ActivityMainBinding
import com.planner.services.SSEService
import com.planner.view.adapter.TarefaAdapter
import com.planner.viewmodel.MainViewModel
import com.planner.viewmodel.SSEViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TarefaAdapter
    private lateinit var viewModel : MainViewModel
    private lateinit var service : SSEService
    private var sseViewModel = SSEViewModel()

    @RequiresApi(Build.VERSION_CODES.O)
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
            sseViewModel = SSEViewModel()
            sseViewModel.getSSEEvents()
            setObservers()
//            // verifica se a conexão ainda está funcional
//            if (sseViewModel.sseEvents.value?.type !in
//                listOf("error", "closed")) {
//                Toast.makeText(this, "Conexão já estabelecida", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            sseViewModel.retryConnection()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObservers(){
        viewModel.getListaTarefas().observe(this){
            adapter.updateTarefas(it)
        }
        viewModel.getTxtToast().observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        // observer da conexão com o Node
        sseViewModel.sseEvents.observe(this) {
            event -> viewModel.eventHandler(event)
        }
    }

    private fun setAdapter(){
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