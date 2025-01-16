package com.nedajalili.wordweave

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LevelDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_details)

        val levelId = intent.getIntExtra("LEVEL_ID", 0)
        val levelTitleText: TextView = findViewById(R.id.levelTitleText)
        levelTitleText.text = "Level $levelId"
    }
}
