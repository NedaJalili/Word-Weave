package com.nedajalili.wordweave

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var partAdapter: PartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

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
            Level(index, "Level $index", index == 1 && partNumber == 1) // فقط مرحله ۱ از پارت ۱ باز است
        }
    }


    private fun onPartSelected(part: Part) {
        val intent = Intent(this, LevelsActivity::class.java)
        intent.putExtra("PART_ID", part.id)
        startActivity(intent)
    }
}
