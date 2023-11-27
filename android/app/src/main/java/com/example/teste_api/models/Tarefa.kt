package com.example.teste_api.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class Tarefa {
    var id : Int = 0
    var titulo: String = ""
    var descricao = ""
    var status = STATUS.PENDENTE
    @RequiresApi(Build.VERSION_CODES.O)
    var dataInicio : LocalDate = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    var dataFinal : LocalDate? = LocalDate.now()
    var categoria : String? = null
    var periodicidade : PERIODOS? = PERIODOS.NENHUMA

    enum class PERIODOS {
        NENHUMA,
        DIARIA,
        SEMANAL,
        MENSAL,
        ANUAL
    }

    enum class STATUS {
        PENDENTE,
        COMPLETA
    }

}