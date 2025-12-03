package com.example.pbo.Nazwar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pbo.R
import com.example.pbo.data.HistoryEntity
import com.example.pbo.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainPageCalculateResults : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main_page_calculate_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ambil data dari Intent
        val name = intent.getStringExtra("deviceName")
        val power = intent.getStringExtra("devicePower")
        val duration = intent.getIntExtra("duration", 0)
        val frequency = intent.getIntExtra("frequency", 0)

        // tombol back
        val buttonBack = findViewById<ImageView>(R.id.panah)
        buttonBack.setOnClickListener { finish() }

        // tampilkan data ke UI
        val deviceName = findViewById<TextView>(R.id.device)
        deviceName.text = name

        val devicePower = findViewById<TextView>(R.id.AmtPower)
        devicePower.text = getString(R.string.label_power, power)

        val deviceDuration = findViewById<TextView>(R.id.AmtDuration)
        deviceDuration.text = getString(R.string.label_duration, duration)

        val deviceFrequency = findViewById<TextView>(R.id.AmtFrequency)
        deviceFrequency.text = getString(R.string.label_frequency, frequency)

        // Perhitungan energi & biaya
        val powerWatt = power?.toDoubleOrNull() ?: 0.0
        val durationMin = duration.toDouble()
        val frequencyWeek = frequency.toDouble()
        val tariff = 1500.0

        val energyPerUse = (powerWatt / 1000.0) * (durationMin / 60.0)     // kWh per use
        val energyPerWeek = energyPerUse * frequencyWeek                   // kWh/week
        val energyPerMonth = energyPerWeek * 4.0                           // kWh/month

        val costPerMonth = energyPerMonth * tariff

        val amountText = findViewById<TextView>(R.id.amount)
        amountText.text = "Rp${"%,.0f".format(costPerMonth)}/month"

        // tombol details
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

        // Simpan ke history (Room) — jalankan di background
        val currentDateString = currentDate()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val db = AppDatabase.getDatabase(this@MainPageCalculateResults)
                val deviceNameVal = name ?: "Unknown device"

                // format nilai kWh & cost sebagai String (sesuai HistoryEntity sekarang)
                val kwhStr = "%.3f kWh".format(energyPerMonth)
                val costStr = "Rp${"%,.0f".format(costPerMonth)}"

                db.historyDao().insert(
                    HistoryEntity(
                        deviceName = deviceNameVal,
                        kWh = kwhStr,
                        totalCost = costStr,
                        date = currentDateString
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace() // debug bila ada kegagalan insert
            }
        }
    }

    // fungsi pembantu membuat timestamp
    private fun currentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}