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
import com.planner.view.adapter.TarefaAdapter
import com.planner.viewmodel.MainViewModel
import com.planner.viewmodel.SSEViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TarefaAdapter
    private lateinit var viewModel : MainViewModel
    private var sseViewModel = SSEViewModel()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TarefaAdapter(this)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.rcvTarefas.layoutManager = LinearLayoutManager(this)

        sseViewModel.getSSEEvents()

        setObservers()
        setAdapter()

        binding.btnNovaTarefa.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        binding.btnRetry.setOnClickListener {
            sseViewModel = SSEViewModel()
            sseViewModel.getSSEEvents()
            setObservers()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAdapter(){
        binding.rcvTarefas.adapter = adapter

        adapter.onItemLongClick = {
            var tarefaTemp = adapter.listaTarefas[it]
            viewModel.excluirTarefa(tarefaTemp)
            viewModel.getTarefasFromDB()
        }

        adapter.onItemClick = {
            var tarefaTemp = adapter.listaTarefas[it]
            var intent = Intent(this, CadastroActivity::class.java)
            intent.putExtra("idTarefa", tarefaTemp.id)
            startActivity(intent)
        }

        adapter.onStatusClick = {
            var tarefa = adapter.listaTarefas[it]

            viewModel.mudarStatus(tarefa)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTarefasFromDB()
    }
}