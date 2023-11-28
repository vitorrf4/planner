package com.example.aula20.services.model

class ValidarTarefas {

    fun verificarCampoVazio(nomeTarefa : String) : Boolean {
        return nomeTarefa.isEmpty()
    }

}