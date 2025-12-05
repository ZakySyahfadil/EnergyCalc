package com.example.pbo.Alim

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R
import com.example.pbo.zaky.LogIn
import com.example.pbo.utils.DialogUtils
import com.example.pbo.data.repository.UserRepositoryImpl

class settingActivity : AppCompatActivity() {

    private val repository by lazy { UserRepositoryImpl(this) }

    private val changeNameLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // UI Update langsung dari result intent (Performance Optimization)
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

        // [ISP - Interface Segregation Principle]
        // Tombol-tombol ini terpisah tugasnya, dan memanggil Activity lain yang spesifik.

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
                onConfirm = { performLogout() }
            )
        }
    }

    private fun updateUserNameDisplay() {
        // Menggunakan Repository untuk mengambil data (DIP)
        val (first, last) = repository.getUserFromPrefs()
        findViewById<TextView>(R.id.tv_nama).text = "$first $last".trim()
    }

    private fun performLogout() {
        repository.logout()
        startActivity(Intent(this, LogIn::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}