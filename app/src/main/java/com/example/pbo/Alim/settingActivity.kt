package com.example.pbo.Alim

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import com.example.ec.changeName
import com.example.pbo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnChangeName = findViewById<Button>(R.id.btnChangeName)
        btnChangeName.setOnClickListener {
            val intent = Intent(this, changeName::class.java)
            startActivity(intent)

        }

        val btnChangePassword = findViewById<Button>(R.id.btn2)
        btnChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }
    }
}