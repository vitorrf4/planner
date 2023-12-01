package com.planner.services

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.planner.misc.LocalDateTimeAdapter
import com.planner.misc.Properties
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

abstract class RetrofitClient {
    companion object {
        private val BASE_URL = Properties.apiUrl
        private lateinit var INSTANCE : Retrofit

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getRetrofitInstance() : Retrofit{
            if(!Companion::INSTANCE.isInitialized) {
                val HTTP = OkHttpClient.Builder()

                // adiciona uma classe que serializa e deserializa o LocalDateTime
                val gson: Gson = GsonBuilder()
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                    .create()

                val factory = GsonConverterFactory.create(gson)

                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(HTTP.build())
                    .addConverterFactory(factory)
                    .build()
            }
            return INSTANCE
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun createMessageService(): TarefaService {
            return getRetrofitInstance().create(TarefaService::class.java)
        }
    }
}