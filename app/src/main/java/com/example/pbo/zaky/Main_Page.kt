package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.Alim.settingActivity
import com.example.pbo.R

class Main_Page : AppCompatActivity() {

    private lateinit var tvName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)

        tvName = findViewById(R.id.name)
        val editProfile = findViewById<TextView>(R.id.edit)

        loadUserName()   // ⬅️ tampilkan nama pertama kali

        editProfile.setOnClickListener {
            startActivity(Intent(this, settingActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserName()   // ⬅️ REFRESH nama setelah balik dari SettingActivity
    }

    private fun loadUserName() {
        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val firstname = prefs.getString("firstname", "User")
        val lastname = prefs.getString("lastname", "")
        tvName.text = "$firstname $lastname".trim()
    }
}
