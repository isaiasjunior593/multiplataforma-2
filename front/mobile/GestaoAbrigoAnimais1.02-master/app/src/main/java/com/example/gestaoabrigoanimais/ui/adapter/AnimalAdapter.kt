package com.example.gestaoabrigoanimais.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestaoabrigoanimais.DetalhesSaudeActivity
import com.example.gestaoabrigoanimais.R
import com.example.gestaoabrigoanimais.data.model.Animal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AnimalAdapter(private var animais: List<Animal>) :
    RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeRaca: TextView = itemView.findViewById(R.id.tv_item_nome_raca)
        val status: TextView = itemView.findViewById(R.id.tv_item_status)
        val generoData: TextView = itemView.findViewById(R.id.tv_item_genero_data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animais[position]
        val dataFormatada = dateFormat.format(Date(animal.dataRegistro))

        holder.nomeRaca.text = "${animal.nome} - ${animal.raca}"
        holder.status.text = "Status: ${animal.status}"
        holder.generoData.text = "Gênero: ${animal.genero} | Registro: $dataFormatada"

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalhesSaudeActivity::class.java).apply {
                putExtra("ANIMAL_ID", animal.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = animais.size

    fun updateList(newList: List<Animal>) {
        animais = newList
        notifyDataSetChanged()
    }
}