package com.example.dz1android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dz1android.ui.theme.Dz1androidTheme

import android.net.Uri

import android.util.Log
import android.widget.Button
import android.widget.TextView


class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.activity_second)
            val intent = intent
            val data: Uri? = intent.data
            val textView = findViewById<TextView>(R.id.textViewUri)

            if (data != null) {
                textView.text = data.toString()
                Log.d("SecondActivity", "URI обработан: $data")
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Dz1androidTheme {
        Greeting2("Android")
    }
}