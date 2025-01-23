package com.nedajalili.wordweave

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class GameActivity : AppCompatActivity() {
    private var levelId: Int = 0
    private var lives: Int = 5
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var stageData: Map<Int, Pair<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // بارگذاری مرحله جاری از Intent
        levelId = intent.getIntExtra("LEVEL_ID", 0)

        // بارگذاری وضعیت بازی از SharedPreferences
        sharedPreferences = getSharedPreferences("GameData", MODE_PRIVATE)
        lives = sharedPreferences.getInt("LIVES", 5)

        // بارگذاری داده‌های مراحل از فایل JSON
        stageData = loadStageData()

        // پیدا کردن TextView برای نمایش عنوان مرحله
        val levelTitleTextView = findViewById<TextView>(R.id.levelTitle)

        // تنظیم عنوان ثابت "Word Weave"
        levelTitleTextView.text = "Word Weave"

        val wordContainer = findViewById<LinearLayout>(R.id.wordContainer)
        val lettersGrid = findViewById<GridLayout>(R.id.lettersGrid)
        val gameMessage = findViewById<TextView>(R.id.gameMessage)
        val nextLevelButton = findViewById<Button>(R.id.nextLevelButton)
        val livesTextView = findViewById<TextView>(R.id.livesTextView)

        // گرفتن کلمه صحیح و کلمه scrambled برای مرحله جاری
        val (targetWord, scrambledWord) = stageData[levelId] ?: Pair("", "")

        // نمایش دکمه‌ها و حروف
        for (char in scrambledWord) {
            val letterButton = Button(this).apply {
                text = char.toString()
                setOnClickListener {
                    addToWordContainer(this, wordContainer, targetWord, gameMessage, nextLevelButton, lettersGrid)
                }
            }
            lettersGrid.addView(letterButton)
        }

        // مدیریت دکمه مرحله بعدی
        nextLevelButton.setOnClickListener {
            if (levelId < stageData.size - 1 && isLevelUnlocked(levelId + 1)) {
                levelId++ // حرکت به مرحله بعد
                val nextLevelIntent = intent
                nextLevelIntent.putExtra("LEVEL_ID", levelId)
                finish()  // بسته شدن مرحله جاری و باز شدن مرحله بعدی
                startActivity(nextLevelIntent)
            } else {
                // اگر مرحله بعدی قفل باشد
                Toast.makeText(this, "Unlock previous level to proceed", Toast.LENGTH_SHORT).show()
            }
        }

        // مدیریت بازگشت به عقب
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        })

        // نمایش تعداد جان‌ها
        livesTextView.text = "Lives: $lives"
    }

    private fun loadStageData(): Map<Int, Pair<String, String>> {
        val stageData = mutableMapOf<Int, Pair<String, String>>()
        val jsonString = assets.open("levels.json").bufferedReader().use { it.readText() }
        val levelsArray = JSONArray(JSONObject(jsonString).getString("levels"))

        for (i in 0 until levelsArray.length()) {
            val level = levelsArray.getJSONObject(i)
            val id = level.getInt("id")
            val word = level.getString("word")
            val scrambledLetters = level.getJSONArray("scrambledLetters")
            val scrambledWord = (0 until scrambledLetters.length()).joinToString("") { scrambledLetters.getString(it) }

            stageData[id] = Pair(word, scrambledWord)
        }

        return stageData
    }

    private fun addToWordContainer(
        button: Button,
        wordContainer: LinearLayout,
        targetWord: String,
        gameMessage: TextView,
        nextLevelButton: Button,
        lettersGrid: GridLayout
    ) {
        button.isEnabled = false

        val letter = button.text.toString()
        val letterView = TextView(this).apply {
            text = letter
            textSize = 24f
            textAlignment = View.TEXT_ALIGNMENT_GRAVITY
        }
        wordContainer.addView(letterView)

        val currentWord = (0 until wordContainer.childCount)
            .joinToString("") { (wordContainer.getChildAt(it) as TextView).text.toString() }

        val livesTextView = findViewById<TextView>(R.id.livesTextView)
        livesTextView.text = "Lives: $lives"

        if (currentWord.length == targetWord.length) {
            if (currentWord == targetWord) {
                gameMessage.text = "Congratulations! You completed the level!"
                gameMessage.visibility = View.VISIBLE
                nextLevelButton.visibility = View.VISIBLE
                lettersGrid.visibility = View.GONE
                unlockNextLevel(levelId + 1)  // باز کردن مرحله بعدی
            } else {
                lives--  // کاهش جان در صورت اشتباه
                Toast.makeText(this, "Incorrect! Lives remaining: $lives", Toast.LENGTH_SHORT).show()
                if (lives == 0) {
                    showGameOverDialog()
                } else {
                    resetWordContainer(wordContainer, lettersGrid)
                }
            }
        }
    }

    private fun resetWordContainer(wordContainer: LinearLayout, lettersGrid: GridLayout) {
        wordContainer.removeAllViews()
        for (i in 0 until lettersGrid.childCount) {
            val button = lettersGrid.getChildAt(i) as Button
            button.isEnabled = true
        }
    }

    private fun showGameOverDialog() {
        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("You have lost all your lives. Try again!")
            .setPositiveButton("Retry") { _, _ -> recreate() }
            .setNegativeButton("Exit") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Game")
            .setMessage("Are you sure you want to exit the game?")
            .setPositiveButton("Yes") { _, _ ->
                saveGameState()
                finish()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun unlockNextLevel(levelId: Int) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("LEVEL_${levelId}", true)
        editor.apply()
    }

    private fun isLevelUnlocked(levelId: Int): Boolean {
        return sharedPreferences.getBoolean("LEVEL_${levelId}", false)
    }

    private fun saveGameState() {
        val editor = sharedPreferences.edit()
        editor.putInt("LIVES", lives)
        editor.putInt("CURRENT_LEVEL", levelId)
        editor.apply()
    }
}
