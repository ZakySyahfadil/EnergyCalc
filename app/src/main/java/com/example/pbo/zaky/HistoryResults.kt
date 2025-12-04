package com.example.pbo.zaky

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R

class HistoryResults : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history_results)

        // Tombol Back
        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        // Ambil data dari Intent (key harus sama dengan yang dikirim)
        val deviceName = intent.getStringExtra("deviceName") ?: ""
        val totalCost = intent.getStringExtra("totalCost") ?: ""
        val kWh = intent.getStringExtra("kWh") ?: ""

        val power = intent.getStringExtra("powerValue") ?: ""
        val duration = intent.getStringExtra("durationValue") ?: ""
        val frequency = intent.getStringExtra("frequencyValue") ?: ""

        // Isi UI (sesuai desain xml mu)
        findViewById<TextView>(R.id.device).text = deviceName
        findViewById<TextView>(R.id.amount).text = totalCost
        findViewById<TextView>(R.id.AmtPower).text = power
        findViewById<TextView>(R.id.AmtDuration).text = duration
        findViewById<TextView>(R.id.AmtFrequency).text = frequency
    }
}