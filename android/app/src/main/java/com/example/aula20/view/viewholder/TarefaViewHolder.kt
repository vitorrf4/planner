package com.example.aula20.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aula20.R

class TarefaViewHolder(tarefaLayout: View) : RecyclerView.ViewHolder(tarefaLayout) {
    var txtNomeTarefa = tarefaLayout.findViewById<TextView>(R.id.txtNomeTarefa)
}