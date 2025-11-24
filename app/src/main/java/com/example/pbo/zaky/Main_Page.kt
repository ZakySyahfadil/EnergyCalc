package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pbo.Alim.settingActivity
import com.example.pbo.R

class Main_Page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)

        val editProfile = findViewById<TextView>(R.id.edit)
        editProfile.setOnClickListener {
            val intent = Intent(this, settingActivity::class.java)
            startActivity(intent)
        }
    }
}