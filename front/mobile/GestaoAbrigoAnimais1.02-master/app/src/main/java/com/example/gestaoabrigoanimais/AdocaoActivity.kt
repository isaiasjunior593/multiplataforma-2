package com.example.gestaoabrigoanimais

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.gestaoabrigoanimais.data.database.AppDatabase
import com.example.gestaoabrigoanimais.data.model.Adotante
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdocaoActivity : AppCompatActivity() {

    private lateinit var nomeEditText: EditText
    private lateinit var telefoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var enderecoEditText: EditText
    private lateinit var moradiaRadioGroup: RadioGroup
    private lateinit var outrosAnimaisCheckbox: CheckBox
    private lateinit var telaSegurancaCheckbox: CheckBox
    private lateinit var intencaoAdocaoEditText: EditText
    private lateinit var enviarButton: Button

    private lateinit var database: AppDatabase
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adocao)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "abrigo-db"
        )
            .fallbackToDestructiveMigration() // CORREÇÃO DE MIGRAÇÃO APLICADA
            .build()

        encontrarViews()

        enviarButton.setOnClickListener {
            enviarSolicitacao()
        }
    }

    private fun encontrarViews() {
        nomeEditText = findViewById(R.id.et_adotante_nome)
        telefoneEditText = findViewById(R.id.et_adotante_telefone)
        emailEditText = findViewById(R.id.et_adotante_email)
        enderecoEditText = findViewById(R.id.et_adotante_endereco)
        moradiaRadioGroup = findViewById(R.id.rg_tipo_moradia)
        outrosAnimaisCheckbox = findViewById(R.id.cb_tem_outros_animais)
        telaSegurancaCheckbox = findViewById(R.id.cb_possui_tela)
        intencaoAdocaoEditText = findViewById(R.id.et_intencao_adocao)
        enviarButton = findViewById(R.id.btn_enviar_adocao)
    }

    private fun enviarSolicitacao() {
        val nome = nomeEditText.text.toString().trim()
        val telefone = telefoneEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val endereco = enderecoEditText.text.toString().trim()

        if (nome.isBlank() || telefone.isBlank() || endereco.isBlank()) {
            Toast.makeText(this, "Preencha Nome, Telefone e Endereço.", Toast.LENGTH_LONG).show()
            return
        }

        val moradiaId = moradiaRadioGroup.checkedRadioButtonId
        val tipoMoradia = if (moradiaId != -1) {
            val radioButton: RadioButton = findViewById(moradiaId)
            radioButton.text.toString()
        } else {
            "Não Especificado"
        }

        val temOutrosAnimais = outrosAnimaisCheckbox.isChecked
        val possuiTela = telaSegurancaCheckbox.isChecked
        val intencaoAdocao = intencaoAdocaoEditText.text.toString().trim()

        val novoAdotante = Adotante(
            nomeCompleto = nome,
            telefone = telefone,
            email = email,
            enderecoCompleto = endereco,
            tipoMoradia = tipoMoradia,
            temOutrosAnimais = temOutrosAnimais,
            possuiTelaJanela = possuiTela,
            intencaoAdocao = intencaoAdocao
        )

        scope.launch {
            try {
                database.adotanteDao().insert(novoAdotante)

                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Solicitação de adoção enviada! Responderemos em breve.", Toast.LENGTH_LONG).show()
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Erro ao salvar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}