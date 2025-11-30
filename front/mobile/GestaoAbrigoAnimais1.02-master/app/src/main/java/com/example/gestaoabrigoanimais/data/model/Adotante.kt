package com.example.gestaoabrigoanimais.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "adotantes")
data class Adotante(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nomeCompleto: String,
    val telefone: String,
    val email: String,
    val enderecoCompleto: String,
    val tipoMoradia: String, // Casa, Apartamento (alugado/próprio)
    val temOutrosAnimais: Boolean,
    val possuiTelaJanela: Boolean, // Essencial para gatos
    val intencaoAdocao: String, // Referência ao Animal que ele quer adotar
    val statusAprovacao: String = "Pendente" // Pendente, Aprovado, Rejeitado
)