package com.example.pbo.Nazwar

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pbo.R

class Main_Page_Calculate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page_calculate)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Logika switch
        val toggle = findViewById<Switch>(R.id.switch_toggle)
        val warning = findViewById<LinearLayout>(R.id.text_view)

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
