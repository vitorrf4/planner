package com.planner.models

import java.time.LocalDateTime

class ValidarTarefas {
    fun verificarCampoVazio(titulo: String, descricao: String, data: LocalDateTime) : Boolean {
        return titulo.isBlank() || descricao.isBlank() || data == null
    }
}