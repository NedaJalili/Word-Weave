package com.nedajalili.wordweave

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LevelsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var levelsAdapter: LevelsAdapter
    private lateinit var partTitle: TextView
    private var partId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_levels) // این باید با فایل XML شما تطابق داشته باشد

        partId = intent.getIntExtra("PART_ID", 0)

        // ارتباط با ویوها
        recyclerView = findViewById(R.id.levelsRecyclerView)
        partTitle = findViewById(R.id.partTitle)

        // نمایش عنوان پارت
        partTitle.text = "Part $partId Levels"

        // تنظیم RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val levels = loadLevelsForPart(partId)
        levelsAdapter = LevelsAdapter(levels) { level ->
            onLevelSelected(level)
        }
        recyclerView.adapter = levelsAdapter
    }

    private fun loadLevelsForPart(partId: Int): List<Level> {
        // دریافت مراحل بر اساس شناسه پارت
        val start = (partId - 1) * 10 + 1
        return (start until start + 10).map { index ->
            val isUnlocked = isLevelUnlocked(index)
            Level(index, "Level $index", isUnlocked)
        }
    }

    private fun isLevelUnlocked(levelId: Int): Boolean {
        val sharedPreferences = getSharedPreferences("GameData", MODE_PRIVATE)
        return sharedPreferences.getBoolean("LEVEL_$levelId", levelId == 1)  // فقط مرحله 1 به طور پیش‌فرض باز است
    }


    private fun onLevelSelected(level: Level) {
        if (!level.isUnlocked) return

        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("LEVEL_ID", level.id)
        startActivity(intent)
    }
}
