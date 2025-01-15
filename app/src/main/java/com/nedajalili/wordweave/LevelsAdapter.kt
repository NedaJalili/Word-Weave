package com.nedajalili.wordweave

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LevelsAdapter(
    private val levels: List<Level>,
    private val onLevelSelected: (Level) -> Unit // Lambda برای ارسال رویداد
) : RecyclerView.Adapter<LevelsAdapter.LevelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_level, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        val level = levels[position]
        holder.levelNumber.text = level.id.toString()
        holder.levelTitle.text = level.title

        if (level.isLocked) {
            holder.lockIcon.visibility = View.VISIBLE
            holder.levelTitle.setTextColor(android.graphics.Color.GRAY)
        } else {
            holder.lockIcon.visibility = View.GONE
            holder.levelTitle.setTextColor(android.graphics.Color.BLACK)

            // تنظیم کلیک روی آیتم
            holder.itemView.setOnClickListener {
                onLevelSelected(level) // ارسال سطح انتخاب‌شده به Lambda
            }
        }
    }

    override fun getItemCount(): Int = levels.size

    class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val levelNumber: TextView = itemView.findViewById(R.id.levelNumber)
        val levelTitle: TextView = itemView.findViewById(R.id.levelTitle)
        val lockIcon: ImageView = itemView.findViewById(R.id.lockIcon)
    }
}
