package com.example.teuxdeux

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.login).setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }

        findViewById<Button>(R.id.signup).setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}