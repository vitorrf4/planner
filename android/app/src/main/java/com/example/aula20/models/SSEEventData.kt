package com.example.aula20.models

data class SSEEventData(
    val eventStatus: EVENT_STATUS? = null,
    var tarefa: Tarefa? = null
)

enum class EVENT_STATUS {
    NONE,
    OPEN,
    SUCCESS,
    CLOSED,
    ERROR
}