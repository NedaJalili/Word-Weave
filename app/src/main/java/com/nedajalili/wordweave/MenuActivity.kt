package com.nedajalili.wordweave

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.SharedPreferences


class MenuActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var partAdapter: PartAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        sharedPreferences = getSharedPreferences("GameData", MODE_PRIVATE)

        recyclerView = findViewById(R.id.partsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val parts = loadParts()
        partAdapter = PartAdapter(parts) { part ->
            onPartSelected(part)
        }
        recyclerView.adapter = partAdapter
    }

    private fun loadParts(): List<Part> {
        return listOf(
            Part(1, "Part 1", generateLevelsForPart(1)),
            Part(2, "Part 2", generateLevelsForPart(2)),
            Part(3, "Part 3", generateLevelsForPart(3))
        )
    }

    private fun generateLevelsForPart(partNumber: Int): List<Level> {
        val start = (partNumber - 1) * 10 + 1
        return (start until start + 10).map { index ->
            val isLevelUnlocked = isLevelUnlocked(index)
            Level(index, "Level $index", isLevelUnlocked)
        }
    }

    private fun isLevelUnlocked(levelId: Int): Boolean {
        return sharedPreferences.getBoolean("LEVEL_$levelId", levelId == 1)
    }

    private fun onPartSelected(part: Part) {
        val intent = Intent(this, LevelsActivity::class.java)
        intent.putExtra("PART_ID", part.id)
        startActivity(intent)
    }

    fun unlockNextLevel(levelId: Int) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("LEVEL_${levelId + 1}", true)
        editor.apply()
    }
}
