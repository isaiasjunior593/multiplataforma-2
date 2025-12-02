package com.example.gestaoabrigoanimais

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
// import androidx.room.Room // (Opcional: Pode manter se quiser salvar localmente também)
// import com.example.gestaoabrigoanimais.data.database.AppDatabase // (Local DB)
// import com.example.gestaoabrigoanimais.data.model.Animal // (Local Model)
import com.example.gestaoabrigoanimais.ui.adapter.MiniaturaFotoAdapter

// --- NOVOS IMPORTS PARA A API ---
import com.example.gestaoabrigoanimais.api.AnimalRequest
import com.example.gestaoabrigoanimais.api.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var nomeEditText: EditText
    private lateinit var racaEditText: EditText
    private lateinit var generoRadioGroup: RadioGroup
    private lateinit var statusSpinner: Spinner
    private lateinit var salvarButton: Button
    private lateinit var verListaButton: Button
    private lateinit var castradoCheckBox: CheckBox
    private lateinit var observacoesEditText: EditText
    private lateinit var iniciarAdocaoButton: Button

    private lateinit var selecionarFotosButton: Button
    private lateinit var miniaturasRecyclerView: RecyclerView
    private lateinit var fotoAdapter: MiniaturaFotoAdapter
    private var selectedImageUris: MutableList<Uri> = mutableListOf()

    private var statusSelecionado: String = ""

    // private lateinit var database: AppDatabase // (Banco local desativado para focar na API)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(4)) { uris ->
            if (uris.isNotEmpty()) {
                selectedImageUris.clear()
                selectedImageUris.addAll(uris)
                fotoAdapter.updateUris(selectedImageUris)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        // Configuração do Banco Local (Comentado para usar a API)
        /*
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "abrigo-db"
        )
            .fallbackToDestructiveMigration()
            .build()
        */

        encontrarViews()
        configurarSpinnerStatus()
        configurarSelecaoFotos()

        salvarButton.setOnClickListener {
            coletarDadosESalvarNaAPI() // <--- Mudamos o nome da função aqui
        }

        verListaButton.setOnClickListener {
            val intent = Intent(this, ListaAnimaisActivity::class.java)
            startActivity(intent)
        }

        iniciarAdocaoButton.setOnClickListener {
            val intent = Intent(this, AdocaoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Lembretes de Vacinas"
            val descriptionText = "Notificações para datas de próximas vacinas de animais"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("vaccine_reminder_channel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun encontrarViews() {
        nomeEditText = findViewById(R.id.et_nome_animal)
        racaEditText = findViewById(R.id.et_raca_animal)
        generoRadioGroup = findViewById(R.id.rg_genero)
        statusSpinner = findViewById(R.id.spn_status_animal)
        salvarButton = findViewById(R.id.btn_salvar_ficha)
        verListaButton = findViewById(R.id.btn_ver_lista)
        castradoCheckBox = findViewById(R.id.cb_castrado)
        observacoesEditText = findViewById(R.id.et_observacoes_saude)
        iniciarAdocaoButton = findViewById(R.id.btn_iniciar_adocao)

        selecionarFotosButton = findViewById(R.id.btn_selecionar_fotos)
        miniaturasRecyclerView = findViewById(R.id.rv_miniaturas_fotos)
    }

    private fun configurarSpinnerStatus() {
        val opcoesStatus = arrayOf("Em Adoção", "Em Tratamento", "Lar Temporário", "Adotado")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoesStatus)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = adapter

        statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                statusSelecionado = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                statusSelecionado = opcoesStatus[0]
            }
        }
    }

    private fun configurarSelecaoFotos() {
        miniaturasRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        fotoAdapter = MiniaturaFotoAdapter(selectedImageUris)
        miniaturasRecyclerView.adapter = fotoAdapter

        selecionarFotosButton.setOnClickListener {
            pickMultipleMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    }

    // --- NOVA FUNÇÃO: SALVAR NA API (Nuvem) ---
    private fun coletarDadosESalvarNaAPI() {
        val nome = nomeEditText.text.toString().trim()
        val raca = racaEditText.text.toString().trim()

        if (nome.isBlank() || raca.isBlank()) {
            Toast.makeText(this, "Nome e Raça são obrigatórios!", Toast.LENGTH_SHORT).show()
            return
        }

        val radioIdSelecionado = generoRadioGroup.checkedRadioButtonId
        val genero = if (radioIdSelecionado != -1) {
            val radioButton: RadioButton = findViewById(radioIdSelecionado)
            radioButton.text.toString()
        } else {
            "Macho" // Default para a API não quebrar
        }

        // Nota: O backend atual recebe Nome, Raça, Gênero, Castrado e Obs.
        // Status e Fotos ainda não estão no modelo da API, então não enviaremos agora.
        val isCastrado = castradoCheckBox.isChecked
        val observacoes = observacoesEditText.text.toString().trim()

        // Cria o objeto que a API espera
        val novoAnimalRequest = AnimalRequest(
            nome = nome,
            raca = raca,
            genero = genero,
            castrado = isCastrado,
            obs = observacoes
        )

        // Inicia a chamada de rede em background
        scope.launch {
            try {
                // Chama o Backend Node.js
                val response = RetrofitClient.instance.salvarAnimal(novoAnimalRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Sucesso! Animal salvo na Nuvem ☁️", Toast.LENGTH_LONG).show()
                        limparCampos()
                    } else {
                        Toast.makeText(applicationContext, "Erro na API: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Se o servidor estiver desligado ou o IP errado
                    Toast.makeText(applicationContext, "Sem conexão: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun limparCampos() {
        nomeEditText.setText("")
        racaEditText.setText("")
        castradoCheckBox.isChecked = false
        observacoesEditText.setText("")
        selectedImageUris.clear()
        fotoAdapter.updateUris(emptyList())
        generoRadioGroup.clearCheck()
    }
}
