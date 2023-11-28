package com.example.teste_api.models

import com.example.aula20.services.model.Tarefa

data class SSEEventData(
    val eventStatus: EVENT_STATUS? = null,
    var tarefa: Tarefa? = null
)

enum class EVENT_STATUS {
    SUCCESS,
    ERROR,
    NONE,
    CLOSED,
    OPEN
}