package com.nedajalili.wordweave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.nedajalili.wordweave.ui.theme.WordWeaveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordWeaveTheme {
                StartGameScreen()
            }
        }
    }
}

@Composable
fun StartGameScreen() {
    // طراحی صفحه اصلی با یک دکمه در مرکز
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { /* عمل مربوط به دکمه */ }) {
            Text("Start Game")
        }
    }
}
