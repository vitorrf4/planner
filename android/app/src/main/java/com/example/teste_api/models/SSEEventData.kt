package com.example.teste_api.models

data class SSEEventData(
    val eventStatus: EVENT_STATUS? = null,
    var message: String? = null
)

enum class EVENT_STATUS {
    SUCCESS,
    ERROR,
    NONE,
    CLOSED,
    OPEN
}