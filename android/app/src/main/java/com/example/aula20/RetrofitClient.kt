package com.example.aula20

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class RetrofitClient {
    companion object {
        private val BASE_URL = Properties.apiUrl
        private lateinit var INSTANCE : Retrofit

        private fun getRetrofitInstance() : Retrofit{
            if(!Companion::INSTANCE.isInitialized) {
                val HTTP = OkHttpClient.Builder()

                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(HTTP.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return INSTANCE
        }

        fun createMessageService(): TarefaService {
            return getRetrofitInstance().create(TarefaService::class.java)
        }
    }
}