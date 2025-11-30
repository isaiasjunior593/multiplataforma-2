package com.example.gestaoabrigoanimais.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestaoabrigoanimais.data.model.TarefaVoluntario

@Dao
interface TarefaDao {
    @Insert
    suspend fun insert(tarefa: TarefaVoluntario)

    @Update
    suspend fun update(tarefa: TarefaVoluntario)

    // Busca todas as tarefas, ordenando prioridade (Alta primeiro) e data de criação
    @Query("SELECT * FROM tarefas ORDER BY prioridade DESC, dataCriacao ASC")
    suspend fun getAll(): List<TarefaVoluntario>
}