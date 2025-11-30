package com.example.gestaoabrigoanimais

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.gestaoabrigoanimais.data.database.AppDatabase
import com.example.gestaoabrigoanimais.data.model.Animal
import com.example.gestaoabrigoanimais.utils.VaccineScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetalhesSaudeActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private val scope = CoroutineScope(Dispatchers.IO)
    private var animalId: Int = -1
    private var animalAtual: Animal? = null

    private lateinit var nomeTextView: TextView
    private lateinit var ultimaVacinaEditText: EditText
    private lateinit var proximaVacinaTextView: TextView
    private lateinit var observacoesTextView: TextView
    private lateinit var salvarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_saude)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "abrigo-db"
        )
            .fallbackToDestructiveMigration()
            .build()

        animalId = intent.getIntExtra("ANIMAL_ID", -1)

        if (animalId == -1) {
            Toast.makeText(this, "Animal inválido.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        encontrarViews()
        loadDetalhes()

        ultimaVacinaEditText.setOnClickListener { showDatePicker() }
        salvarButton.setOnClickListener { salvarDataVacina() }
    }

    private fun encontrarViews() {
        nomeTextView = findViewById(R.id.tv_nome_animal_saude)
        ultimaVacinaEditText = findViewById(R.id.et_ultima_vacina)
        proximaVacinaTextView = findViewById(R.id.tv_proxima_vacina)
        observacoesTextView = findViewById(R.id.tv_observacoes_saude_detalhe)
        salvarButton = findViewById(R.id.btn_salvar_saude)
    }

    private fun loadDetalhes() {
        scope.launch {
            animalAtual = database.animalDao().getById(animalId)

            withContext(Dispatchers.Main) {
                animalAtual?.let {
                    nomeTextView.text = "Saúde de: ${it.nome}"
                    observacoesTextView.text = if (it.observacoesSaude.isNullOrBlank()) "Nenhuma observação registrada." else it.observacoesSaude

                    if (it.ultimaVacinaData != null) {
                        ultimaVacinaEditText.setText(it.ultimaVacinaData)
                        calcularProximaVacina(it.ultimaVacinaData)
                    } else {
                        ultimaVacinaEditText.setText("")
                        proximaVacinaTextView.text = "Data não registrada. Alerta pendente."
                    }
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dataSelecionada = sdf.format(calendar.time)

            ultimaVacinaEditText.setText(dataSelecionada)
            calcularProximaVacina(dataSelecionada)
        }

        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun calcularProximaVacina(ultimaVacinaData: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val data = sdf.parse(ultimaVacinaData) ?: return

            val calendar = Calendar.getInstance()
            calendar.time = data

            calendar.add(Calendar.DAY_OF_YEAR, 365)

            val proximaData = sdf.format(calendar.time)
            proximaVacinaTextView.text = "Alerta em: $proximaData"
        } catch (e: Exception) {
            proximaVacinaTextView.text = "Erro ao calcular data."
        }
    }

    private fun salvarDataVacina() {
        val novaDataVacina = ultimaVacinaEditText.text.toString()
        val proximaDataVacina = proximaVacinaTextView.text.toString().substringAfter("Alerta em: ")

        if (novaDataVacina.isBlank() || animalAtual == null) {
            Toast.makeText(this, "Selecione uma data antes de salvar.", Toast.LENGTH_SHORT).show()
            return
        }

        val animalAtualizado = animalAtual!!.copy(
            ultimaVacinaData = novaDataVacina,
            proximaVacinaData = proximaDataVacina
        )

        scope.launch {
            database.animalDao().update(animalAtualizado)

            VaccineScheduler.scheduleVaccineReminder(applicationContext, animalAtualizado)

            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Data de vacina atualizada e alerta registrado.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}