package com.example.pbo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ResetPass : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_pass)

        // Tombol back
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        // Input
        val inputNewPass = findViewById<EditText>(R.id.inputEmail) // new password
        val inputConfirm = findViewById<EditText>(R.id.newConfirm) // confirm new password

        // Error TextView
        val tvEmptyNewPass = findViewById<TextView>(R.id.tvEmailError)
        val tvPassMust = findViewById<TextView>(R.id.tvEmailMust)
        val tvEmptyConfirm = findViewById<TextView>(R.id.confirmNewPass)
        val tvIncorrectConfirm = findViewById<TextView>(R.id.incorrectNewPass)

        // Tombol Save
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {

            // Ambil input user
            val newPass = inputNewPass.text.toString().trim()
            val confirmPass = inputConfirm.text.toString().trim()

            // Sembunyikan semua error dulu
            tvEmptyNewPass.visibility = TextView.GONE
            tvPassMust.visibility = TextView.GONE
            tvEmptyConfirm.visibility = TextView.GONE
            tvIncorrectConfirm.visibility = TextView.GONE

            // 1️⃣ Cek new password kosong
            if (newPass.isEmpty()) {
                tvEmptyNewPass.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            // 2️⃣ Cek syarat password minimal 7 huruf + 1 angka
            val hasLetter = newPass.any { it.isLetter() }
            val hasNumber = newPass.any { it.isDigit() }
            if (newPass.length < 7 || !hasLetter || !hasNumber) {
                tvPassMust.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            // 3️⃣ Cek confirm password kosong
            if (confirmPass.isEmpty()) {
                tvEmptyConfirm.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            // 4️⃣ Cek confirm password benar atau tidak
            if (newPass != confirmPass) {
                tvIncorrectConfirm.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            // 5️⃣ Semua benar → simpan new password ke SharedPreferences
            val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE).edit()
            sharedPref.putString("password", newPass)
            sharedPref.apply()

            Toast.makeText(this, "Password has been reset successfully.", Toast.LENGTH_SHORT).show()

            // 6️⃣ Ke halaman LogIn
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }
    }
}