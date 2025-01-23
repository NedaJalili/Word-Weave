package com.nedajalili.wordweave

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LevelsAdapter(
    private val levels: List<Level>,
    private val onLevelSelected: (Level) -> Unit
) : RecyclerView.Adapter<LevelsAdapter.LevelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_level, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        val level = levels[position]
        holder.levelNumber.text = level.id.toString()
        holder.levelTitle.text = level.title

        // بررسی وضعیت قفل یا باز بودن مرحله با استفاده از SharedPreferences
        val sharedPreferences = holder.itemView.context.getSharedPreferences("GameData", 0)
        val isLevelUnlocked = sharedPreferences.getBoolean("LEVEL_${level.id}", level.id == 1) // مرحله 1 همیشه باز است

        if (!isLevelUnlocked) {
            holder.lockIcon.visibility = View.VISIBLE
            holder.levelTitle.setTextColor(Color.GRAY)
            holder.itemView.isEnabled = false // غیرفعال کردن انتخاب مرحله قفل‌شده
        } else {
            holder.lockIcon.visibility = View.GONE
            holder.levelTitle.setTextColor(Color.BLACK)

            // اجازه دادن به کاربر برای انتخاب مرحله اگر باز باشد
            holder.itemView.setOnClickListener {
                onLevelSelected(level)
            }

            holder.itemView.isEnabled = true // فعال کردن انتخاب مرحله برای مراحل باز
        }
    }

    override fun getItemCount(): Int = levels.size

    class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val levelNumber: TextView = itemView.findViewById(R.id.levelNumber)
        val levelTitle: TextView = itemView.findViewById(R.id.levelTitle)
        val lockIcon: ImageView = itemView.findViewById(R.id.lockIcon)
    }
}

