package com.example.teste_api.models

import com.google.gson.annotations.SerializedName

class Message {
    @SerializedName("message")
    var message: String = ""

    constructor(message: String) {
        this.message = message
    }
}