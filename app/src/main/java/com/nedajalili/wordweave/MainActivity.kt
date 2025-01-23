package com.nedajalili.wordweave

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // شیء MediaPlayer برای پخش صدا
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // پیدا کردن دکمه
        val startButton = findViewById<Button>(R.id.startButton)

        // بارگذاری فایل صوتی
        mediaPlayer = MediaPlayer.create(this, R.raw.click1)

        // تنظیم کلیک دکمه
        startButton.setOnClickListener {
            // پخش صدا
            mediaPlayer.start()

            // اجرای انیمیشن تکان خوردن
            val shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_button)
            startButton.startAnimation(shakeAnimation)

            // تغییر رنگ پس‌زمینه
            val colorAnimator = ObjectAnimator.ofObject(
                startButton,
                "backgroundColor",
                ArgbEvaluator(),
                Color.WHITE,
                Color.parseColor("#BB86FC")
            )
            colorAnimator.duration = 500 // مدت زمان تغییر رنگ
            colorAnimator.start()

            // انتقال به MenuActivity
            startButton.postDelayed({
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }, 1000) // تأخیر برای انیمیشن
        }
    }

    // آزادسازی منابع بعد از تمام شدن فعالیت
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
