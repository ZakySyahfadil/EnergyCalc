package com.example.pbo.zaky

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.zaky.CodeOtp
import com.example.pbo.R

class EmailForgot : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_email_forgot)

        // WAJIB sama dengan Login & SignUp3
        prefs = getSharedPreferences("UserData", MODE_PRIVATE)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val emailInput = findViewById<EditText>(R.id.emailedit)

        val signFail = findViewById<TextView>(R.id.signfail)       // Email kosong
        val emailFormat = findViewById<TextView>(R.id.emailformat) // Format salah

        val btnSendCode = findViewById<Button>(R.id.buttonSendCode)

        btnBack.setOnClickListener {
            finish()
        }

        btnSendCode.setOnClickListener {

            val email = emailInput.text.toString().trim()

            // Reset error
            signFail.visibility = TextView.GONE
            emailFormat.visibility = TextView.GONE

            var valid = true

            // 1️⃣ Email kosong
            if (email.isEmpty()) {
                signFail.text = "Please enter your email."
                signFail.visibility = TextView.VISIBLE
                valid = false
            }

            // 2️⃣ Format email salah
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailFormat.text = "Invalid email format."
                emailFormat.visibility = TextView.VISIBLE
                valid = false
            }

            // 3️⃣ Email tidak terdaftar (HARUS terdaftar untuk forgot pass)
            else if (!emailRegistered(email)) {
                signFail.text = "Email not registered."
                signFail.visibility = TextView.VISIBLE
                valid = false
            }

            // Jika semua valid → lanjut ke OTP
            if (valid) {
                val intent = Intent(this, CodeOtp::class.java)
                intent.putExtra("email", email)
                intent.putExtra("mode", "forgot") // OPTIONAL: kalau kamu mau bedakan mode signup/forgot
                startActivity(intent)
            }
        }
    }

    // 🔥 Cek apakah email terdaftar
    private fun emailRegistered(email: String): Boolean {
        val users = prefs.getStringSet("USED_EMAILS", emptySet()) ?: emptySet()
        return users.contains(email)
    }
}