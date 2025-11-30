package com.example.gestaoabrigoanimais

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.gestaoabrigoanimais.data.database.AppDatabase
import com.example.gestaoabrigoanimais.data.model.Adotante
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalhesAdocaoActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private val scope = CoroutineScope(Dispatchers.IO)
    private var adotanteId: Int = -1

    private lateinit var nomeTextView: TextView
    private lateinit var contatoTextView: TextView
    private lateinit var enderecoTextView: TextView
    private lateinit var moradiaTextView: TextView
    private lateinit var compatibilidadeTextView: TextView
    private lateinit var intencaoTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var aprovarButton: Button
    private lateinit var rejeitarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_adocao)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "abrigo-db"
        )
            .fallbackToDestructiveMigration() // CORREÇÃO DE MIGRAÇÃO APLICADA
            .build()

        adotanteId = intent.getIntExtra("ADOTANTE_ID", -1)

        if (adotanteId == -1) {
            Toast.makeText(this, "Solicitação inválida.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        encontrarViews()
        loadDetalhes()

        aprovarButton.setOnClickListener { atualizarStatus("Aprovado") }
        rejeitarButton.setOnClickListener { atualizarStatus("Rejeitado") }
    }

    private fun encontrarViews() {
        nomeTextView = findViewById(R.id.tv_detalhe_nome)
        contatoTextView = findViewById(R.id.tv_detalhe_contato)
        enderecoTextView = findViewById(R.id.tv_detalhe_endereco)
        moradiaTextView = findViewById(R.id.tv_detalhe_moradia)
        compatibilidadeTextView = findViewById(R.id.tv_detalhe_compatibilidade)
        intencaoTextView = findViewById(R.id.tv_detalhe_intencao)
        statusTextView = findViewById(R.id.tv_detalhe_status)
        aprovarButton = findViewById(R.id.btn_aprovar_adocao)
        rejeitarButton = findViewById(R.id.btn_rejeitar_adocao)
    }

    private fun loadDetalhes() {
        scope.launch {
            val adotante = database.adotanteDao().getById(adotanteId)

            withContext(Dispatchers.Main) {
                adotante?.let {
                    nomeTextView.text = it.nomeCompleto
                    contatoTextView.text = "Tel: ${it.telefone} | Email: ${it.email}"
                    enderecoTextView.text = "Endereço: ${it.enderecoCompleto}"
                    moradiaTextView.text = "Moradia: ${it.tipoMoradia}"
                    intencaoTextView.text = "Interesse: ${it.intencaoAdocao}"
                    statusTextView.text = "Status: ${it.statusAprovacao}"

                    val comp = "Outros Pets: ${if (it.temOutrosAnimais) "Sim" else "Não"} | Telas: ${if (it.possuiTelaJanela) "Sim" else "Não"}"
                    compatibilidadeTextView.text = comp

                    if (it.statusAprovacao != "Pendente") {
                        aprovarButton.isEnabled = false
                        rejeitarButton.isEnabled = false
                    }
                }
            }
        }
    }

    private fun atualizarStatus(novoStatus: String) {
        scope.launch {
            database.adotanteDao().updateStatus(adotanteId, novoStatus)

            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Solicitação $novoStatus.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}