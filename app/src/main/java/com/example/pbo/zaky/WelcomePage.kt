package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.Nazwar.mainpage.MainPage
import com.example.pbo.R

class WelcomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome_page)

        val nameText = findViewById<TextView>(R.id.nameText)

        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val firstname = prefs.getString("firstname", "User")
        val lastname = prefs.getString("lastname", "")

        nameText.text = "$firstname $lastname".trim()

        window.decorView.postDelayed({
            startActivity(Intent(this, MainPage::class.java))
            finish()
        }, 2000)
    }
}
