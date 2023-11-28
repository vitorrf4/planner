package com.planner.models

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