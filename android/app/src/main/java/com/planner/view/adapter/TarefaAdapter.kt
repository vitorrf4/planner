package com.planner.view.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.planner.R
import com.planner.models.Tarefa
import com.planner.view.viewholder.TarefaViewHolder
import java.time.format.DateTimeFormatter

class TarefaAdapter(var context: Context) : RecyclerView.Adapter<TarefaViewHolder>() {
    lateinit var listaTarefas : List<Tarefa>
    var onItemLongClick : ((Int) -> Unit)? = null
    var onItemClick : ((Int) -> Unit)? = null
    var onStatusClick : ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val tarefaLayout = LayoutInflater.from(context)
            .inflate(R.layout.tarefa_layout, parent, false)

        return TarefaViewHolder(tarefaLayout)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        var tarefa = listaTarefas[position]

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        var dataFormatada = tarefa.dataFinal.format(formatter)


        holder.txtNomeTarefa.text = "Titulo: ${tarefa.titulo}\n" +
                "Data limite: $dataFormatada\n " +
                "Status: ${tarefa.status.toString()}"

        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(position)
            true
        }

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(position)
        }

        holder.btnStatus.setOnClickListener {
            onStatusClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return listaTarefas.size
    }

    fun updateTarefas(list : List<Tarefa>){
        listaTarefas = list
        notifyDataSetChanged()
    }

}