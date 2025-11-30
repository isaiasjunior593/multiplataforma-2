package com.example.gestaoabrigoanimais

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setupNavigationCards()
    }

    private fun setupNavigationCards() {
        val cardNovoAnimal: CardView = findViewById(R.id.card_novo_animal)
        val cardGerenciarAdocao: CardView = findViewById(R.id.card_gerenciar_adocao)
        val cardSaudeAlertas: CardView = findViewById(R.id.card_saude_alertas)
        val cardVoluntarios: CardView = findViewById(R.id.card_voluntarios)

        cardNovoAnimal.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        cardGerenciarAdocao.setOnClickListener {
            val intent = Intent(this, GerenciamentoAdocaoActivity::class.java)
            startActivity(intent)
        }

        cardSaudeAlertas.setOnClickListener {
            val intent = Intent(this, ListaAnimaisActivity::class.java)
            startActivity(intent)
        }

        cardVoluntarios.setOnClickListener {
            val intent = Intent(this, VoluntariosActivity::class.java)
            startActivity(intent)
        }
    }
}