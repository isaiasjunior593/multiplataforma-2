package com.example.gestaoabrigoanimais.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestaoabrigoanimais.data.model.Animal

@Dao
interface AnimalDao {
    @Insert
    suspend fun insert(animal: Animal)

    @Query("SELECT * FROM animais ORDER BY nome ASC")
    suspend fun getAll(): List<Animal>

    @Query("SELECT * FROM animais WHERE id = :animalId")
    suspend fun getById(animalId: Int): Animal?

    @Update
    suspend fun update(animal: Animal)

    @Query("SELECT * FROM animais WHERE proximaVacinaData IS NOT NULL AND proximaVacinaData < date('now', '+30 days') ORDER BY proximaVacinaData ASC")
    suspend fun getProximasVacinas(): List<Animal>
}