package com.planner.view

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.planner.databinding.ActivityCadastroBinding
import com.planner.models.FormataData
import com.planner.models.Tarefa
import com.planner.viewmodel.CadastroViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var cadastroViewModel: CadastroViewModel
    private lateinit var tarefaFromDB : Tarefa
    private var idTarefa : Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // inicializar o cadastroViewModel
        cadastroViewModel = ViewModelProvider(this).get(CadastroViewModel::class.java)
        // setar observadores
        setObservers()

        // vamos tentar receber um id vindo da intent
        idTarefa = intent.getIntExtra("idTarefa", idTarefa)

        // verificar o valor de idTarefa
        if (idTarefa > 0) {
            cadastroViewModel.findTarefa(idTarefa)
            binding.txtTitulo.text = "EDITAR TAREFA"
        }

        // ação de clique do botão salvar
        binding.btnSalvar.setOnClickListener {
            var titulo = binding.edtNomeTarefa.text.toString()
            var descricao = binding.edtDescricao.text.toString()
            var data = LocalDate.parse(binding.edtDataFinal.text.toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))

            var dataFinal = LocalDateTime.of(data, LocalTime.parse("00:00"))

            if (idTarefa > 0) {
                editarTarefa(titulo, descricao, dataFinal)
                return@setOnClickListener
            }

            cadastroViewModel.salvarTarefa(titulo, descricao, dataFinal)

            finish()
        }
    }

    private fun editarTarefa(titulo: String, descricao: String, dataFinal: LocalDateTime) {
        tarefaFromDB.titulo = titulo
        tarefaFromDB.descricao = descricao
        tarefaFromDB.dataFinal = dataFinal

        if (cadastroViewModel.atualizarTarefa(tarefaFromDB)) {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setObservers() {
        cadastroViewModel.getTxtToast().observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        cadastroViewModel.getTarefaFromDB().observe(this){
            tarefaFromDB = it
            binding.edtNomeTarefa.setText(tarefaFromDB.titulo)
            binding.edtDescricao.setText(tarefaFromDB.descricao)
            binding.edtDataFinal.setText(FormataData.formatar(tarefaFromDB.dataFinal.toLocalDate()))
        }

    }
}