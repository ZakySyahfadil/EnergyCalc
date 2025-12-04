package com.example.pbo.zaky

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R

class HistoryDetails : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_details)

        val btnBack = findViewById<ImageView>(R.id.bttnback)

        btnBack.setOnClickListener { finish() }

        // Kamu bisa lanjutkan isi detail di step berikutnya
    }
}
