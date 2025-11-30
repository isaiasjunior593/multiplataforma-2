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
import androidx.room.Room
import com.example.gestaoabrigoanimais.data.database.AppDatabase
import com.example.gestaoabrigoanimais.data.model.Animal
import com.example.gestaoabrigoanimais.ui.adapter.MiniaturaFotoAdapter
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

    private lateinit var database: AppDatabase
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

        createNotificationChannel() // Cria o canal de notificação para alertas

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "abrigo-db"
        )
            .fallbackToDestructiveMigration()
            .build()

        encontrarViews()
        configurarSpinnerStatus()
        configurarSelecaoFotos()

        salvarButton.setOnClickListener {
            coletarDadosESalvar()
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

    private fun coletarDadosESalvar() {
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
            "Não Informado"
        }

        val status = statusSelecionado

        val isCastrado = castradoCheckBox.isChecked
        val observacoes = observacoesEditText.text.toString().trim()

        val fotosParaSalvar = selectedImageUris.map { it.toString() }

        val novoAnimal = Animal(
            nome = nome,
            raca = raca,
            genero = genero,
            status = status,
            isCastrado = isCastrado,
            observacoesSaude = observacoes,
            fotosUris = fotosParaSalvar
        )

        scope.launch {
            database.animalDao().insert(novoAnimal)

            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Animal $nome SALVO com sucesso!", Toast.LENGTH_LONG).show()

                nomeEditText.setText("")
                racaEditText.setText("")
                castradoCheckBox.isChecked = false
                observacoesEditText.setText("")

                selectedImageUris.clear()
                fotoAdapter.updateUris(emptyList())
            }
        }
    }
}