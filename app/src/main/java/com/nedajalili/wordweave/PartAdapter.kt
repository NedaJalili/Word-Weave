package com.nedajalili.wordweave

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PartAdapter(
    private val parts: List<Part>,
    private val onPartSelected: (Part) -> Unit
) : RecyclerView.Adapter<PartAdapter.PartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_part, parent, false)
        return PartViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        val part = parts[position]
        holder.partTitle.text = part.title

        holder.itemView.setOnClickListener {
            onPartSelected(part)
        }
    }

    override fun getItemCount(): Int = parts.size

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val partTitle: TextView = itemView.findViewById(R.id.partTitle)
    }
}
