package com.haman.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(".MainActivity", "onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.d(".MainActivity", "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(".MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(".MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(".MainActivity", "onDestroy")
    }
}