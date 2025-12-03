package com.example.pbo.Nazwar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pbo.R

class MainPageCalculateResults : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout. activity_main_page_calculate_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //memasukkan nilai variabel ke layar
        val name = intent.getStringExtra("deviceName")
        val power = intent.getStringExtra("devicePower")
        val duration = intent.getIntExtra("duration", 0)
        val frequency = intent.getIntExtra("frequency", 0)

        val deviceName = findViewById<TextView>(R.id.device)
        deviceName.text = name

        val devicePower = findViewById<TextView>(R.id.AmtPower)
        devicePower.text = getString(R.string.label_power, power)

        val deviceDuration = findViewById<TextView>(R.id.AmtDuration)
        deviceDuration.text = getString(R.string.label_duration, duration)

        val deviceFrequency = findViewById<TextView>(R.id.AmtFrequency)
        deviceFrequency.text = getString(R.string.label_frequency, frequency)

        //menghitung jumlah biaya listrik per bulan
        val powerWatt = power?.toDoubleOrNull() ?: 0.0
        val durationMin = duration.toDouble()
        val frequencyWeek = frequency.toDouble()

        // Tarif listrik per kWh
        val tariff = 1500

        // Perhitungan energi
        val energyPerUse = (powerWatt / 1000) * (durationMin / 60)   // kWh
        val energyPerWeek = energyPerUse * frequencyWeek               // kWh/week
        val energyPerMonth = energyPerWeek * 4                       // kWh/month

        // Biaya
        val costPerMonth = energyPerMonth * tariff

        // Set ke TextView
        val amountText = findViewById<TextView>(R.id.amount)
        amountText.text = "Rp${"%,.0f".format(costPerMonth)}/month"

        //logika details button
        val btnDetail = findViewById<Button>(R.id.btn_detail)

        btnDetail.setOnClickListener {
            val intent = Intent(this, MainPageCalculateDetails::class.java)

            intent.putExtra("power", power)
            intent.putExtra("duration", duration)
            intent.putExtra("frequency", frequency)
            intent.putExtra("costPerMonth", costPerMonth)
            intent.putExtra("energyPerWeek", energyPerWeek)

            startActivity(intent)
        }
        //tombol back
    }
}