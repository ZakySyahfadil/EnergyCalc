package com.example.pbo.Alim

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R
import com.example.pbo.zaky.LogIn
import com.example.pbo.utils.DialogUtils // Pastikan Import ini ada!

class settingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)

        // Setup Nama User
        updateUserNameDisplay()

        // Tombol Change Name
        findViewById<Button>(R.id.btnChangeName).setOnClickListener {
            startActivity(Intent(this, ChangeName::class.java))
        }

        // Tombol Change Password
        findViewById<Button>(R.id.btn2).setOnClickListener {
            startActivity(Intent(this, ChangePassword::class.java))
        }

        // Tombol Logout (Menggunakan DialogUtils)
        findViewById<Button>(R.id.btn3).setOnClickListener {
            DialogUtils.showUniversalDialog(
                context = this,
                message = "Are you sure you want to log out?",
                isConfirmation = true,
                onConfirm = {
                    logoutUser()
                }
            )
        }

        // Tombol Back
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    private fun updateUserNameDisplay() {
        val tvNama = findViewById<TextView>(R.id.tv_nama)
        val nameFromIntent = intent.getStringExtra("USER_NAME")

        val sharedPref = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val firstname = sharedPref.getString("firstname", null)
        val lastname = sharedPref.getString("lastname", null)

        val fullNameFromPref = if (firstname != null && lastname != null) {
            "$firstname $lastname".trim()
        } else null

        tvNama.text = when {
            nameFromIntent != null -> nameFromIntent
            fullNameFromPref != null -> fullNameFromPref
            else -> "Guest"
        }
    }

    private fun logoutUser() {
        // Hapus semua data login
        getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE).edit().clear().apply()

        startActivity(Intent(this, LogIn::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    override fun onResume() {
        super.onResume()
        updateUserNameDisplay()
    }
}