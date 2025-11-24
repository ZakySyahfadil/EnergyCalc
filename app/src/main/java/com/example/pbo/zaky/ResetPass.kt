package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import kotlinx.coroutines.launch

class ResetPass : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_pass)

        // Ambil email user dari Intent
        val userEmail = intent.getStringExtra("email") ?: ""

        // Tombol back
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        // Input password
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

            // 4️⃣ Cek confirm password sama dengan new password
            if (newPass != confirmPass) {
                tvIncorrectConfirm.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            // 5️⃣ Semua valid → update password di Room Database
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@ResetPass)
                db.accountDao().updatePasswordByEmail(userEmail, newPass)

                runOnUiThread {
                    Toast.makeText(
                        this@ResetPass,
                        "Password has been reset successfully.",
                        Toast.LENGTH_SHORT
                    ).show()

                    // 6️⃣ Pindah ke halaman LogIn
                    val intent = Intent(this@ResetPass, LogIn::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}