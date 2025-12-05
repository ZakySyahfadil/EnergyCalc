package com.example.pbo.Nazwar

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pbo.Alim.HistoryActivity
import com.example.pbo.Alim.settingActivity
import com.example.pbo.Nazwar.data.UserPreferences
import com.example.pbo.Nazwar.repository.UserRepositoryImpl
import com.example.pbo.R
import com.google.android.material.button.MaterialButton

class MainPage : AppCompatActivity() {

    private lateinit var userRepo: UserRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        // Init user data (SOLID DIP)
        val prefs = UserPreferences(this)
        userRepo = UserRepositoryImpl(prefs)

        // Set name
        val tvName = findViewById<TextView>(R.id.name)
        tvName.text = userRepo.getFullName()

        findViewById<TextView>(R.id.edit).setOnClickListener {
            startActivity(Intent(this, settingActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.button_calculate).setOnClickListener {
            startActivity(Intent(this, MainPageCalculate::class.java))
        }

        findViewById<MaterialButton>(R.id.buttonHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.name).text = userRepo.getFullName()
    }
}