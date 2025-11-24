package com.example.pbo.Alim

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import com.example.pbo.Alim.ChangeName
import com.example.pbo.R
import com.example.pbo.zaky.LogIn
import com.google.android.material.button.MaterialButton

class settingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)

        val btnChangeName = findViewById<Button>(R.id.btnChangeName)
        btnChangeName.setOnClickListener {
            val intent = Intent(this, ChangeName::class.java)
            startActivity(intent)

        }

        val btnChangePassword = findViewById<Button>(R.id.btn2)
        btnChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }

        val btnLogOut = findViewById<Button>(R.id.btn3)
        btnLogOut.setOnClickListener {
            val dialog = Dialog(this, R.style.DialogNoBorder)
            dialog.setContentView(R.layout.dialog_logout_confirm)
            dialog.setCancelable(true) // bisa dismiss dengan tap luar

            val btnYes = dialog.findViewById<MaterialButton>(R.id.btnYes)
            val btnNo = dialog.findViewById<MaterialButton>(R.id.btnNo)

            btnYes.setOnClickListener {
                dialog.dismiss()
                // Lakukan proses logout di sini
                logoutUser() // fungsi kamu sendiri
            }

            btnNo.setOnClickListener {
                dialog.dismiss() // batal logout
            }

            dialog.window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val params = attributes
                params.gravity = Gravity.CENTER
                attributes = params
            }

            dialog.show()
        }
    }
    private fun logoutUser() {
        // 1. Hapus semua data login (SharedPreferences, Session, dll)
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // 2. Pindah ke halaman Login + hapus semua activity sebelumnya
        val intent = Intent(this, LogIn::class.java) // ganti LoginActivity dengan nama class login kamu
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // tutup settingActivity
    }
}