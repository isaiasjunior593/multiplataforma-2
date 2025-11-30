package com.example.gestaoabrigoanimais.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestaoabrigoanimais.DetalhesAdocaoActivity
import com.example.gestaoabrigoanimais.R
import com.example.gestaoabrigoanimais.data.model.Adotante

class AdotanteAdapter(private var adotantes: List<Adotante>) :
    RecyclerView.Adapter<AdotanteAdapter.AdotanteViewHolder>() {

    class AdotanteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.tv_adotante_nome)
        val contato: TextView = itemView.findViewById(R.id.tv_adotante_contato)
        val intencao: TextView = itemView.findViewById(R.id.tv_adotante_intencao)
        val status: TextView = itemView.findViewById(R.id.tv_adotante_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdotanteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adotante, parent, false)
        return AdotanteViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdotanteViewHolder, position: Int) {
        val adotante = adotantes[position]

        holder.nome.text = adotante.nomeCompleto
        holder.contato.text = "${adotante.telefone} | ${adotante.email}"
        holder.intencao.text = "Interesse: ${adotante.intencaoAdocao}"
        holder.status.text = "Status: ${adotante.statusAprovacao}"

        // Lógica de Clique: Lança a tela de Detalhes
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalhesAdocaoActivity::class.java).apply {
                putExtra("ADOTANTE_ID", adotante.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = adotantes.size

    fun updateList(newList: List<Adotante>) {
        adotantes = newList
        notifyDataSetChanged()
    }
}