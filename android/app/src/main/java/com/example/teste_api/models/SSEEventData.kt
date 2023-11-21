package com.example.teste_api.models

data class SSEEventData(
    val status: STATUS? = null,
    var message: String? = null
)

enum class STATUS {
    SUCCESS,
    ERROR,
    NONE,
    CLOSED,
    OPEN
}