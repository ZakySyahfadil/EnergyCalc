package com.example.pbo.Alim

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R
import com.example.pbo.zaky.LogIn
import com.example.pbo.utils.DialogUtils

class settingActivity : AppCompatActivity() {

    // 🔥 MENERIMA HASIL DARI CHANGE NAME
    private val changeNameLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val newName = result.data?.getStringExtra("UPDATED_NAME")
                if (newName != null) {
                    findViewById<TextView>(R.id.tv_nama).text = newName
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)

        updateUserNameDisplay()

        // 🔥 BUKA CHANGE NAME PAKAI LAUNCHER
        findViewById<Button>(R.id.btnChangeName).setOnClickListener {
            val intent = Intent(this, ChangeName::class.java)
            changeNameLauncher.launch(intent)
        }

        findViewById<Button>(R.id.btn2).setOnClickListener {
            startActivity(Intent(this, ChangePassword::class.java))
        }

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

        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    private fun updateUserNameDisplay() {
        val tvNama = findViewById<TextView>(R.id.tv_nama)

        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val first = prefs.getString("firstname", "")
        val last = prefs.getString("lastname", "")

        tvNama.text = "$first $last".trim()
    }

    private fun logoutUser() {
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
