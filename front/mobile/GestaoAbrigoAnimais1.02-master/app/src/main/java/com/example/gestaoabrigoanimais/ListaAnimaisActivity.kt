package com.example.gestaoabrigoanimais

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.gestaoabrigoanimais.data.database.AppDatabase
import com.example.gestaoabrigoanimais.ui.adapter.AnimalAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaAnimaisActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnimalAdapter
    private lateinit var database: AppDatabase

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_animais)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "abrigo-db"
        )
            .fallbackToDestructiveMigration() // CORREÇÃO DE MIGRAÇÃO APLICADA
            .build()

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadAnimais()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_animais)
        adapter = AnimalAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadAnimais() {
        scope.launch {
            val animais = database.animalDao().getAll()

            withContext(Dispatchers.Main) {
                adapter.updateList(animais)
            }
        }
    }
}