// GameActivity.kt
package com.nedajalili.wordweave

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

class GameActivity : AppCompatActivity() {
    private var levelId: Int = 0
    private var lives: Int = 5
    private val stageData = mapOf(
        1 to Pair("BOOK", "OKOB"),
        2 to Pair("GAME", "EMGA"),
        3 to Pair("PLAY", "LYAP")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        levelId = intent.getIntExtra("LEVEL_ID", 0)

        val wordContainer = findViewById<LinearLayout>(R.id.wordContainer)
        val lettersGrid = findViewById<GridLayout>(R.id.lettersGrid)
        val gameMessage = findViewById<TextView>(R.id.gameMessage)
        val nextLevelButton = findViewById<Button>(R.id.nextLevelButton)

        val (targetWord, scrambledWord) = stageData[levelId] ?: Pair("", "")

        for (char in scrambledWord) {
            val letterButton = Button(this).apply {
                text = char.toString()
                setOnClickListener {
                    addToWordContainer(this, wordContainer, targetWord, gameMessage, nextLevelButton, lettersGrid)
                }
            }
            lettersGrid.addView(letterButton)
        }

        nextLevelButton.setOnClickListener {
            unlockNextLevel(levelId)
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        })
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
        }
        wordContainer.addView(letterView)

        val currentWord = (0 until wordContainer.childCount)
            .joinToString("") { (wordContainer.getChildAt(it) as TextView).text.toString() }

        if (currentWord.length == targetWord.length) {
            if (currentWord == targetWord) {
                gameMessage.text = "Congratulations! You completed the level!"
                gameMessage.visibility = View.VISIBLE
                nextLevelButton.visibility = View.VISIBLE
                lettersGrid.visibility = View.GONE
            } else {
                lives--
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
            .setPositiveButton("Retry") { _, _ ->
                recreate()
            }
            .setNegativeButton("Exit") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Game")
            .setMessage("Are you sure you want to exit the game?")
            .setPositiveButton("Yes") { _, _ ->
                unlockNextLevel(levelId)
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun unlockNextLevel(levelId: Int) {
        val sharedPreferences = getSharedPreferences("GameData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("LEVEL_${levelId + 1}", true)
        editor.apply()
    }
}
