package com.nedajalili.wordweave

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // اتصال به فایل XML مربوط به MainActivity

        // پیدا کردن دکمه و تنظیم کلیک
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            // انتقال به MenuActivity
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}
