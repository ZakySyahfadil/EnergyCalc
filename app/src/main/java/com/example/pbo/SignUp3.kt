package com.example.pbo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button

class SignUp3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up3)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val firstName = findViewById<EditText>(R.id.firstname)
        val lastName = findViewById<EditText>(R.id.lastname)
        val password = findViewById<EditText>(R.id.passsign)

        val failName = findViewById<TextView>(R.id.failfirst)
        val failPass = findViewById<TextView>(R.id.failpass)
        val passCondition = findViewById<TextView>(R.id.PassCondition)

        val sendCodeBtn = findViewById<Button>(R.id.newsign)

        // Ambil email dari halaman SignUp1
        val email = intent.getStringExtra("email")

        // Tombol Back
        btnBack.setOnClickListener {
            finish()
        }

        // Tombol Sign Up
        sendCodeBtn.setOnClickListener {

            val first = firstName.text.toString().trim()
            val last = lastName.text.toString().trim()
            val pass = password.text.toString().trim()

            var valid = true

            // Cek nama kosong
            if (first.isEmpty() || last.isEmpty()) {
                failName.visibility = TextView.VISIBLE
                valid = false
            } else {
                failName.visibility = TextView.GONE
            }

            // Cek password kosong
            if (pass.isEmpty()) {
                failPass.visibility = TextView.VISIBLE
                passCondition.visibility = TextView.GONE
                valid = false
            } else {
                failPass.visibility = TextView.GONE
            }

            // Validasi syarat password
            if (pass.isNotEmpty() && !isPasswordValid(pass)) {
                passCondition.visibility = TextView.VISIBLE
                valid = false
            } else {
                passCondition.visibility = TextView.GONE
            }

            if (valid) {

                // ===================================
                //   SIMPAN DATA KE SHAREDPREFERENCES
                // ===================================
                val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.putString("email", email)
                editor.putString("firstname", first)
                editor.putString("lastname", last)
                editor.putString("password", pass)
                editor.apply()
                // ===================================

                val fullName = "$first $last"
                val intent = Intent(this, WelcomePage::class.java)
                intent.putExtra("USER_NAME", fullName)
                startActivity(intent)
            }
        }
    }

    // Fungsi Validasi Password
    private fun isPasswordValid(pass: String): Boolean {
        val hasNumber = pass.any { it.isDigit() }
        val hasLetters = pass.count { it.isLetter() } >= 7
        return hasNumber && hasLetters
    }
}