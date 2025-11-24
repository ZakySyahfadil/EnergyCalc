package com.example.pbo.Alim

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R
import com.example.pbo.zaky.LogIn
import com.google.android.material.button.MaterialButton

class settingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)

        val tvNama = findViewById<TextView>(R.id.tv_nama)

        val nameFromIntent = intent.getStringExtra("USER_NAME")

        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
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

        // Button Change Name
        findViewById<Button>(R.id.btnChangeName).setOnClickListener {
            startActivity(Intent(this, ChangeName::class.java))
        }

        // Button Change Password
        findViewById<Button>(R.id.btn2).setOnClickListener {
            startActivity(Intent(this, ChangePassword::class.java))
        }

        // Button Log Out
        findViewById<Button>(R.id.btn3).setOnClickListener {
            showLogoutDialog()
        }

        // Tombol back
        findViewById<android.widget.ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    private fun showLogoutDialog() {
        val dialog = Dialog(this, R.style.DialogNoBorder)
        dialog.setContentView(R.layout.dialog_logout_confirm)
        dialog.setCancelable(true)

        val btnYes = dialog.findViewById<MaterialButton>(R.id.btnYes)
        val btnNo = dialog.findViewById<MaterialButton>(R.id.btnNo)

        btnYes.setOnClickListener {
            dialog.dismiss()
            logoutUser()
        }
        btnNo.setOnClickListener { dialog.dismiss() }

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.gravity = Gravity.CENTER
        }
        dialog.show()
    }

    private fun logoutUser() {
        // Hapus semua data login
        getSharedPreferences("UserData", Context.MODE_PRIVATE).edit().clear().apply()
        getSharedPreferences("user_session", Context.MODE_PRIVATE).edit().clear().apply()

        startActivity(Intent(this, LogIn::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}