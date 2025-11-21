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
import com.example.pbo.zaky.SignUp2

class SignUp : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        // 🔥 WAJIB SAMA DENGAN LOGIN & SIGNUP3
        prefs = getSharedPreferences("UserData", MODE_PRIVATE)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val emailInput = findViewById<EditText>(R.id.emailedit)

        val signFail = findViewById<TextView>(R.id.signfail)
        val emailFormat = findViewById<TextView>(R.id.emailformat)
        val emailUsed = findViewById<TextView>(R.id.emailused)

        val btnSendCode = findViewById<Button>(R.id.buttonSendCode)

        btnBack.setOnClickListener {
            finish()
        }

        btnSendCode.setOnClickListener {

            val email = emailInput.text.toString().trim()

            // Reset error
            signFail.visibility = TextView.GONE
            emailFormat.visibility = TextView.GONE
            emailUsed.visibility = TextView.GONE

            var valid = true

            if (email.isEmpty()) {
                signFail.visibility = TextView.VISIBLE
                valid = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailFormat.visibility = TextView.VISIBLE
                valid = false
            } else if (emailAlreadyUsed(email)) {
                emailUsed.visibility = TextView.VISIBLE
                valid = false
            }

            if (valid) {
                saveEmail(email)

                val intent = Intent(this, SignUp2::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
            }
        }
    }

    private fun emailAlreadyUsed(email: String): Boolean {
        val savedSet = prefs.getStringSet("USED_EMAILS", emptySet()) ?: emptySet()
        return savedSet.contains(email)
    }

    private fun saveEmail(email: String) {
        val savedSet = prefs.getStringSet("USED_EMAILS", emptySet())?.toMutableSet() ?: mutableSetOf()
        savedSet.add(email)
        prefs.edit().putStringSet("USED_EMAILS", savedSet).apply()
    }
}