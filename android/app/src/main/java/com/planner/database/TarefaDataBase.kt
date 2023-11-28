package com.planner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.planner.models.DateConverter
import com.planner.models.Tarefa

@TypeConverters(DateConverter::class)
@Database(entities = [Tarefa::class], version = 2)
abstract class TarefaDataBase : RoomDatabase() {

    abstract fun getDAO() : TarefaDAO

    companion object{

        private lateinit var INSTANCE : TarefaDataBase

        fun getInstance(context: Context) : TarefaDataBase {

            if (!Companion::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(context, TarefaDataBase::class.java, "tarefas_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return INSTANCE
        }


    }

}