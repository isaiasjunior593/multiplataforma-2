package com.example.gestaoabrigoanimais

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.gestaoabrigoanimais.data.database.AppDatabase
import com.example.gestaoabrigoanimais.data.model.TarefaVoluntario
import com.example.gestaoabrigoanimais.ui.adapter.TarefaAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class VoluntariosActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var tarefaAdapter: TarefaAdapter

    private lateinit var tituloEditText: EditText
    private lateinit var descricaoEditText: EditText
    private lateinit var prioridadeSpinner: Spinner
    private lateinit var salvarButton: Button
    private lateinit var tarefasRecyclerView: RecyclerView

    private var prioridadeSelecionada: String = "Baixa"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voluntarios)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "abrigo-db"
        )
            .fallbackToDestructiveMigration()
            .build()

        encontrarViews()
        configurarAdapter()
        configurarSpinner()

        salvarButton.setOnClickListener { salvarNovaTarefa() }
        loadTarefas()
    }

    private fun encontrarViews() {
        tituloEditText = findViewById(R.id.et_titulo_tarefa)
        descricaoEditText = findViewById(R.id.et_descricao_tarefa)
        prioridadeSpinner = findViewById(R.id.spn_prioridade)
        salvarButton = findViewById(R.id.btn_salvar_tarefa)
        tarefasRecyclerView = findViewById(R.id.rv_tarefas)
    }

    private fun configurarAdapter() {
        tarefasRecyclerView.layoutManager = LinearLayoutManager(this)

        // O callback 'atualizarStatusTarefa' será executado quando o checkbox for marcado/desmarcado
        tarefaAdapter = TarefaAdapter(emptyList()) { tarefaAtualizada ->
            atualizarStatusTarefa(tarefaAtualizada)
        }
        tarefasRecyclerView.adapter = tarefaAdapter
    }

    private fun configurarSpinner() {
        // Assume que o array 'prioridades' está em res/values/arrays.xml
        val prioridadesArray = resources.getStringArray(R.array.prioridades)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridadesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioridadeSpinner.adapter = adapter

        prioridadeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                prioridadeSelecionada = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                prioridadeSelecionada = prioridadesArray[0]
            }
        }
    }

    private fun loadTarefas() {
        scope.launch {
            val listaTarefas = database.tarefaDao().getAll()

            withContext(Dispatchers.Main) {
                tarefaAdapter.updateList(listaTarefas)
            }
        }
    }

    private fun salvarNovaTarefa() {
        val titulo = tituloEditText.text.toString().trim()
        val descricao = descricaoEditText.text.toString().trim()

        if (titulo.isBlank()) {
            Toast.makeText(this, "O título da tarefa é obrigatório.", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dataCriacao = sdf.format(Calendar.getInstance().time)

        val novaTarefa = TarefaVoluntario(
            titulo = titulo,
            descricao = descricao,
            prioridade = prioridadeSelecionada,
            dataCriacao = dataCriacao
        )

        scope.launch {
            database.tarefaDao().insert(novaTarefa)

            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Tarefa '$titulo' adicionada.", Toast.LENGTH_SHORT).show()
                tituloEditText.setText("")
                descricaoEditText.setText("")
                prioridadeSpinner.setSelection(0)

                loadTarefas() // Recarrega a lista para mostrar a nova tarefa
            }
        }
    }

    private fun atualizarStatusTarefa(tarefa: TarefaVoluntario) {
        scope.launch {
            database.tarefaDao().update(tarefa)

            withContext(Dispatchers.Main) {
                val status = if (tarefa.concluida) "CONCLUÍDA" else "REABERTA"
                Toast.makeText(applicationContext, "Tarefa '${tarefa.titulo}' marcada como $status.", Toast.LENGTH_SHORT).show()
                loadTarefas() // Recarrega para refletir a mudança na lista
            }
        }
    }
}