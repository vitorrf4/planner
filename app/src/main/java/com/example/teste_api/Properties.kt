package com.example.teste_api

import android.util.Log
import java.io.FileInputStream
import java.util.*

private const val CONFIG = "config.properties"

object Properties {
    private val properties = Properties()

    init {
        val file = FileInputStream(CONFIG)
        Log.d("TESTPROP", file.toString())
        properties.load(file)
    }

    fun getProperty(key: String): String = properties.getProperty(key)
}