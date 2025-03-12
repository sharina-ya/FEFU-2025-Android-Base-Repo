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
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager

import android.widget.Button
import android.widget.TextView
import androidx.compose.material3.Button


class MainActivity : ComponentActivity() {

    private lateinit var textViewCounter: TextView
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewCounter = findViewById(R.id.textViewCounter)
        textViewCounter.text = counter.toString()
        textViewCounter.setOnClickListener {
            counter++
            textViewCounter.text = counter.toString()
        }

        val button = findViewById<Button>(R.id.buttonSecondActivity)
        button.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("counter", counter)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        counter = savedInstanceState.getInt("counter")
        textViewCounter.text = counter.toString()
    }

    private val networkReceiver = NetworkChangeReceiver()
    private val networkIntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    override fun onResume() {
        super.onResume()
        registerReceiver(networkReceiver, networkIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Dz1androidTheme {
        Greeting("Android")
    }
}