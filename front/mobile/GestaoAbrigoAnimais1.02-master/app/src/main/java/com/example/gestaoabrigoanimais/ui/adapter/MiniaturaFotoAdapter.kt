package com.example.gestaoabrigoanimais.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gestaoabrigoanimais.R

class MiniaturaFotoAdapter(private val uris: MutableList<Uri>) :
    RecyclerView.Adapter<MiniaturaFotoAdapter.FotoViewHolder>() {

    class FotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_miniatura_foto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_miniatura_foto, parent, false)
        return FotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: FotoViewHolder, position: Int) {
        val uri = uris[position]
        Glide.with(holder.itemView.context)
            .load(uri)
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = uris.size

    fun updateUris(newUris: List<Uri>) {
        uris.clear()
        uris.addAll(newUris)
        notifyDataSetChanged()
    }
}