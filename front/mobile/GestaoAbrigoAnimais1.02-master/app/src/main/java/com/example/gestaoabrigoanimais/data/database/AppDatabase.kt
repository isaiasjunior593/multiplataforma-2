package com.example.gestaoabrigoanimais.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gestaoabrigoanimais.data.dao.AdotanteDao
import com.example.gestaoabrigoanimais.data.dao.AnimalDao
import com.example.gestaoabrigoanimais.data.dao.TarefaDao
import com.example.gestaoabrigoanimais.data.model.Adotante
import com.example.gestaoabrigoanimais.data.model.Animal
import com.example.gestaoabrigoanimais.data.model.TarefaVoluntario
import com.example.gestaoabrigoanimais.data.database.UriListConverter // Necessário para Animal Entity

@Database(
    entities = [Animal::class, Adotante::class, TarefaVoluntario::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animalDao(): AnimalDao
    abstract fun adotanteDao(): AdotanteDao
    abstract fun tarefaDao(): TarefaDao
}