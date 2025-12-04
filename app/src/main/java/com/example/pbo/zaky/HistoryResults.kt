package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        val device = findViewById<TextView>(R.id.device)
        val amount = findViewById<TextView>(R.id.amount)
        val txtPower = findViewById<TextView>(R.id.AmtPower)
        val txtDuration = findViewById<TextView>(R.id.AmtDuration)
        val txtFrequency = findViewById<TextView>(R.id.AmtFrequency)

        val btnDetails = findViewById<Button>(R.id.btndetails)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // Ambil data yang dikirim dari HistoryActivity
        val deviceName = intent.getStringExtra("deviceName") ?: ""
        val kWh = intent.getStringExtra("kWh") ?: ""
        val totalCost = intent.getStringExtra("totalCost") ?: ""
        val powerValue = intent.getStringExtra("powerValue") ?: ""
        val durationValue = intent.getStringExtra("durationValue") ?: ""
        val frequencyValue = intent.getStringExtra("frequencyValue") ?: ""

        // Set data ke tampilan
        device.text = deviceName
        amount.text = totalCost
        txtPower.text = powerValue
        txtDuration.text = durationValue
        txtFrequency.text = frequencyValue

        // Tombol kembali
        btnBack.setOnClickListener { finish() }

        // 🔥 Button "See Details" menuju HistoryDetails.kt
        btnDetails.setOnClickListener {
            val intent = Intent(this, HistoryDetails::class.java).apply {
                putExtra("deviceName", deviceName)
                putExtra("kWh", kWh)
                putExtra("totalCost", totalCost)
                putExtra("powerValue", powerValue)
                putExtra("durationValue", durationValue)
                putExtra("frequencyValue", frequencyValue)
            }
            startActivity(intent)
        }
    }
}