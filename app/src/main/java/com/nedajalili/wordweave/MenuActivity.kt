package com.nedajalili.wordweave

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var levelsAdapter: LevelsAdapter
    private lateinit var levels: List<Level> // لیست مراحل

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // تنظیم RecyclerView
        recyclerView = findViewById(R.id.levelsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // بارگذاری مراحل و تنظیم Adapter
        levels = loadLevels()
        levelsAdapter = LevelsAdapter(levels)
        recyclerView.adapter = levelsAdapter
    }

    private fun loadLevels(): List<Level> {
        // بارگذاری داده‌ها (اینجا داده‌ها نمونه‌ای هستند)
        return listOf(
            Level(1, "Level 1", false),
            Level(2, "Level 2", true),
            Level(3, "Level 3", true),
            Level(4, "Level 4", true)
        )
    }
}
