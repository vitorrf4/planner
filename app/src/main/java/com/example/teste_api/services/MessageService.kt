package com.example.teste_api.services

import com.example.teste_api.models.Message
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface MessageService {
    @POST("message")
    @Headers("Content-Type: application/json")
    fun addMessage(@Body msg: Message) : Call<Message>
}