package com.example.gestaoabrigoanimais.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarefas")
data class TarefaVoluntario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val descricao: String,
    val prioridade: String, // Baixa, Média, Alta
    val dataCriacao: String,
    val concluida: Boolean = false,
    val voluntarioResponsavel: String? = null
)