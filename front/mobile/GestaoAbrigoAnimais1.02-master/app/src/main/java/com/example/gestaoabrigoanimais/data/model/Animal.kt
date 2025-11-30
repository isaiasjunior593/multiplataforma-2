package com.example.gestaoabrigoanimais.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.gestaoabrigoanimais.data.database.UriListConverter

@Entity(tableName = "animais")
@TypeConverters(UriListConverter::class)
data class Animal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val raca: String,
    val genero: String,
    val status: String,
    val isCastrado: Boolean,
    val observacoesSaude: String,

    val fotosUris: List<String>,
    val ultimaVacinaData: String? = null,
    val proximaVacinaData: String? = null,

    val dataRegistro: Long = System.currentTimeMillis()
)