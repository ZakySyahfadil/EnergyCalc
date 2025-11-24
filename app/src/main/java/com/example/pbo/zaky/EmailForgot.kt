package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import kotlinx.coroutines.launch

class EmailForgot : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_email_forgot)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val emailInput = findViewById<EditText>(R.id.emailedit)

        val signFail = findViewById<TextView>(R.id.signfail)       // Email kosong / not registered
        val emailFormat = findViewById<TextView>(R.id.emailformat) // Format salah

        val btnSendCode = findViewById<Button>(R.id.buttonSendCode)

        btnBack.setOnClickListener { finish() }

        btnSendCode.setOnClickListener {
            val email = emailInput.text.toString().trim()

            // Reset semua error
            signFail.visibility = TextView.GONE
            emailFormat.visibility = TextView.GONE

            // 1️⃣ Email kosong
            if (email.isEmpty()) {
                signFail.text = "Please enter your email."
                signFail.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            // 2️⃣ Format email salah
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailFormat.text = "Invalid email format."
                emailFormat.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            // 3️⃣ Cek email terdaftar di database
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@EmailForgot)
                val account = db.accountDao().getAccountByEmail(email)

                if (account == null) {
                    // Email tidak terdaftar
                    runOnUiThread {
                        signFail.text = "Email not registered."
                        signFail.visibility = TextView.VISIBLE
                    }
                } else {
                    // Email valid dan terdaftar → lanjut ke OTP
                    runOnUiThread {
                        val intent = Intent(this@EmailForgot, CodeOtp::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("mode", "forgot")
                        startActivity(intent)
                    }
                }
            }
        }
    }
}