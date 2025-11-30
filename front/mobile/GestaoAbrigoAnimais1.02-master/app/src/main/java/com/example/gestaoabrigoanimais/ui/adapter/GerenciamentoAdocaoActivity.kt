package com.example.gestaoabrigoanimais

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.gestaoabrigoanimais.data.database.AppDatabase
import com.example.gestaoabrigoanimais.ui.adapter.AdotanteAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GerenciamentoAdocaoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdotanteAdapter
    private lateinit var database: AppDatabase
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerenciamento_adocao)

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
        loadSolicitacoes()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_adotantes)
        adapter = AdotanteAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadSolicitacoes() {
        scope.launch {
            val solicitacoesPendentes = database.adotanteDao().getAllPendentes()
            withContext(Dispatchers.Main) {
                adapter.updateList(solicitacoesPendentes)
            }
        }
    }
}