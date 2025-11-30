package com.example.gestaoabrigoanimais.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gestaoabrigoanimais.R
import com.example.gestaoabrigoanimais.data.model.TarefaVoluntario

class TarefaAdapter(
    private var tarefas: List<TarefaVoluntario>,
    private val onTarefaToggle: (TarefaVoluntario) -> Unit
) : RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder>() {

    class TarefaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.tv_tarefa_titulo)
        val prioridade: TextView = itemView.findViewById(R.id.tv_tarefa_prioridade)
        val data: TextView = itemView.findViewById(R.id.tv_tarefa_data)
        val checkbox: CheckBox = itemView.findViewById(R.id.cb_tarefa_concluida)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarefa_voluntario, parent, false)
        return TarefaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        val tarefa = tarefas[position]

        holder.titulo.text = tarefa.titulo
        holder.data.text = "Criado em: ${tarefa.dataCriacao}"
        holder.checkbox.isChecked = tarefa.concluida
        holder.prioridade.text = "Prioridade: ${tarefa.prioridade}"

        // Define cor da prioridade (requer cores definidas em res/values/colors.xml)
        val context = holder.itemView.context
        val colorRes = when (tarefa.prioridade) {
            "URGENTE" -> R.color.red_priority
            "Alta" -> R.color.orange_priority
            else -> android.R.color.black
        }
        holder.prioridade.setTextColor(ContextCompat.getColor(context, colorRes))

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != tarefa.concluida) {
                // Chama o callback para atualizar o item no banco de dados
                onTarefaToggle(tarefa.copy(concluida = isChecked))
            }
        }
    }

    override fun getItemCount(): Int = tarefas.size

    fun updateList(newList: List<TarefaVoluntario>) {
        // Filtra para mostrar apenas tarefas pendentes por padrão (concluida = false)
        tarefas = newList.filter { !it.concluida }
        notifyDataSetChanged()
    }
}