package com.example.aula20.view.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.aula20.R
import com.example.aula20.models.FormataData
import com.example.aula20.models.Tarefa
import com.example.aula20.view.viewholder.TarefaViewHolder

class TarefaAdapter(var context: Context) : RecyclerView.Adapter<TarefaViewHolder>() {
    lateinit var listaTarefas : List<Tarefa>
    var onItemLongClick : ((Int) -> Unit)? = null
    var onItemClick : ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val tarefaLayout = LayoutInflater.from(context)
            .inflate(R.layout.tarefa_layout, parent, false)

        var holder = TarefaViewHolder(tarefaLayout)

        return holder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        var tarefa = listaTarefas[position]
        holder.txtNomeTarefa.text = "Titulo: ${tarefa.titulo}, Desc: ${tarefa.descricao}\n" +
                "Data:${FormataData.formatar(tarefa.dataFinal.toLocalDate())}\n" +
                "Hora: ${tarefa.dataFinal.toLocalTime()}"

        // ação de clique longo
        holder.itemView.setOnLongClickListener {
            // codigo para excluir tarefa
            onItemLongClick?.invoke(position)
            true
        }

        // ação de clique simples
        holder.itemView.setOnClickListener{
            // codigo para editar tarefa
            onItemClick?.invoke(position)
        }

    }

    override fun getItemCount(): Int {
        return listaTarefas.size
    }

    // método para atualizar a lista de tarefas local
    fun updateTarefas(list : List<Tarefa>){
        listaTarefas = list
        notifyDataSetChanged()
    }

}