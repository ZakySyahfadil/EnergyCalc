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
import com.example.pbo.R

class SignUp : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        // SharedPreferences (WAJIB sama di login / signUp2)
        prefs = getSharedPreferences("UserData", MODE_PRIVATE)

        // UI components
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val input = findViewById<EditText>(R.id.emailedit)

        val tvEmpty = findViewById<TextView>(R.id.signfail)
        val tvFormat = findViewById<TextView>(R.id.emailformat)
        val tvUsed = findViewById<TextView>(R.id.emailused)

        val btnSendCode = findViewById<Button>(R.id.buttonSendCode)

        btnBack.setOnClickListener { finish() }

        btnSendCode.setOnClickListener {

            val userInput = input.text.toString().trim()

            // RESET error messages
            tvEmpty.visibility = TextView.GONE
            tvFormat.visibility = TextView.GONE
            tvUsed.visibility = TextView.GONE

            var valid = true

            // 1️⃣ Tidak boleh kosong
            if (userInput.isEmpty()) {
                tvEmpty.visibility = TextView.VISIBLE
                valid = false

                // 2️⃣ Format harus email ATAU nomor HP
            } else if (!isValidEmail(userInput) && !isValidPhone(userInput)) {
                tvFormat.text = "Format must be a valid email or phone number"
                tvFormat.visibility = TextView.VISIBLE
                valid = false

                // 3️⃣ Cek apakah sudah digunakan
            } else if (identifierUsed(userInput)) {
                tvUsed.visibility = TextView.VISIBLE
                valid = false
            }

            if (valid) {
                saveIdentifier(userInput)

                val intent = Intent(this, SignUp2::class.java)
                intent.putExtra("identifier", userInput)
                startActivity(intent)
            }
        }
    }

    // 🔍 Cek format email
    private fun isValidEmail(input: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    // 🔍 Cek format nomor HP Indonesia (08xxxxxxxx)
    private fun isValidPhone(input: String): Boolean {
        return input.matches(Regex("^08[0-9]{8,12}$"))
    }

    // 📌 Cek apakah email/HP sudah digunakan
    private fun identifierUsed(id: String): Boolean {
        val savedSet = prefs.getStringSet("USED_IDENTIFIERS", emptySet()) ?: emptySet()
        return savedSet.contains(id)
    }

    // 📌 Simpan email/HP
    private fun saveIdentifier(id: String) {
        val savedSet =
            prefs.getStringSet("USED_IDENTIFIERS", emptySet())?.toMutableSet() ?: mutableSetOf()

        savedSet.add(id)

        prefs.edit()
            .putStringSet("USED_IDENTIFIERS", savedSet)
            .apply()
    }
}