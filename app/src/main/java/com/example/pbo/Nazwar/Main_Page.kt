package com.example.pbo.Nazwar

import android.content.Intent
import android.os.Bundle
import android.os.TestLooperManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pbo.Alim.settingActivity
import com.example.pbo.R
import com.google.android.material.button.MaterialButton

class MainPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvName = findViewById<TextView>(R.id.name)
        val editProfile = findViewById<TextView>(R.id.edit)

        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val firstname = prefs.getString("firstname", "User")
        val lastname = prefs.getString("lastname", "")

        tvName.text = "$firstname $lastname".trim()

        editProfile.setOnClickListener {
            startActivity(Intent(this, settingActivity::class.java))
        }

        //Logika Perpindahan
        val buttonCalculate = findViewById<MaterialButton>(R.id.button_calculate)

        buttonCalculate.setOnClickListener {
            val intent = Intent(this, Main_Page_Calculate::class.java)
            startActivity(intent)
        }
    }
}
