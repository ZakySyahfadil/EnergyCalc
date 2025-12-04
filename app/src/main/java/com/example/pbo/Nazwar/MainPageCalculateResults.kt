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
        findViewById<TextView>(R.id.device).text = name
        findViewById<TextView>(R.id.AmtPower).text = getString(R.string.label_power, power)
        findViewById<TextView>(R.id.AmtDuration).text = getString(R.string.label_duration, duration)
        findViewById<TextView>(R.id.AmtFrequency).text = getString(R.string.label_frequency, frequency)

        // Perhitungan energi & biaya
        val powerWatt = power?.toDoubleOrNull() ?: 0.0
        val durationMin = duration.toDouble()
        val frequencyWeek = frequency.toDouble()
        val tariff = 1500.0

        val energyPerUse = (powerWatt / 1000.0) * (durationMin / 60.0) // kWh per use
        val energyPerWeek = energyPerUse * frequencyWeek              // kWh/week
        val energyPerMonth = energyPerWeek * 4.0                      // kWh/month

        val costPerMonth = energyPerMonth * tariff

        // tampilkan biaya ke UI
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

        // ----------------------------------------------------------
        //     FIX: DATA HANYA DISIMPAN KETIKA TOMBOL SAVE DITEKAN
        // ----------------------------------------------------------

        val btnSave = findViewById<Button>(R.id.btn_save)
        btnSave.setOnClickListener {

            val currentDateString = currentDate()

            val powerStr = "$power W"
            val durationStr = "$duration minutes"
            val freqStr = "$frequency times"

            lifecycleScope.launch(Dispatchers.IO) {
                val db = AppDatabase.getDatabase(this@MainPageCalculateResults)

                db.historyDao().insert(
                    HistoryEntity(
                        deviceName = name ?: "Unknown device",
                        kWh = "%.3f kWh".format(energyPerMonth),
                        totalCost = "Rp${"%,.0f".format(costPerMonth)}",
                        date = currentDateString,

                        powerValue = powerStr,
                        durationValue = durationStr,
                        frequencyValue = freqStr
                    )
                )
            }
        }
    }

    // fungsi pembantu membuat timestamp
    private fun currentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}