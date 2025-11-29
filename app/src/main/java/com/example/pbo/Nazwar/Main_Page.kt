package com.example.pbo.Nazwar

import android.content.Intent
import android.os.Bundle
import android.os.TestLooperManager
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        //Logika Perpindahan
        val buttonCalculate = findViewById<MaterialButton>(R.id.button_calculate)

        buttonCalculate.setOnClickListener {
            val intent = Intent(this, Main_Page_Calculate::class.java)
            startActivity(intent)
        }

        //Logika switch
        val toggle = findViewById<Switch>(R.id.switch_toggle)
        val warning = findViewById<TextView>(R.id.text_view)

        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                warning.visibility = View.GONE   // switch ON → text hilang
            } else {
                warning.visibility = View.VISIBLE // switch OFF → text muncul
            }
        }

        //Logika Input
        val deviceName = findViewById<EditText>(R.id.box1)

        val inputText = deviceName.text.toString()


    }
}