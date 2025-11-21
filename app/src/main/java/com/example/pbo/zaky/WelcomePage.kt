package com.example.pbo.zaky

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R

class WelcomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome_page)

        val nameText = findViewById<TextView>(R.id.nameText)

        // Ambil nama dari Intent (untuk SignUp3)
        val nameFromIntent = intent.getStringExtra("USER_NAME")

        // Ambil nama dari SharedPreferences (untuk LogIn)
        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
        val firstname = sharedPref.getString("firstname", null)
        val lastname = sharedPref.getString("lastname", null)

        val fullNameFromPref = if (firstname != null && lastname != null) {
            "$firstname $lastname"
        } else null

        // ============================
        // PRIORITAS TAMPIL NAMA:
        // 1. Jika ada dari Intent → tampilkan
        // 2. Jika tidak ada → pakai SharedPreferences
        // ============================
        nameText.text = when {
            nameFromIntent != null -> nameFromIntent
            fullNameFromPref != null -> fullNameFromPref
            else -> "User"
        }
    }
}