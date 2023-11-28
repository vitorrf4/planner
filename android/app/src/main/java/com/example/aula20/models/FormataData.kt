package com.example.aula20.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FormataData {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun formatar(data: LocalDate): String? {
            return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", ))
        }
    }
}