package com.planner.models

class ValidarTarefas {

    fun verificarCampoVazio(nomeTarefa : String) : Boolean {
        return nomeTarefa.isEmpty()
    }

}