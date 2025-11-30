package com.example.gestaoabrigoanimais.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestaoabrigoanimais.data.model.Adotante

@Dao
interface AdotanteDao {
    @Insert
    suspend fun insert(adotante: Adotante)

    @Query("SELECT * FROM adotantes ORDER BY statusAprovacao ASC")
    suspend fun getAllPendentes(): List<Adotante>

    @Query("SELECT * FROM adotantes WHERE id = :adotanteId")
    suspend fun getById(adotanteId: Int): Adotante?

    @Query("UPDATE adotantes SET statusAprovacao = :novoStatus WHERE id = :adotanteId")
    suspend fun updateStatus(adotanteId: Int, novoStatus: String)
}