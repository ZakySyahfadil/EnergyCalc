package com.example.pbo.Nazwar

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pbo.Nazwar.formatter.CalculationFormatter
import com.example.pbo.R

class MainPageCalculateDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page_calculate_details)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // === Ambil data ===
        val power = intent.getIntExtra("power", 0)
        val durationMinutes = intent.getIntExtra("duration", 0)
        val frequency = intent.getIntExtra("frequency", 0)
        val energyPerWeek = intent.getDoubleExtra("energyPerWeek", 0.0)
        val costPerMonth = intent.getDoubleExtra("costPerMonth", 0.0)

        val durationHour = durationMinutes / 60.0

        // === Tombol kembali ===
        findViewById<ImageView>(R.id.panah).setOnClickListener { finish() }

        // === Tampilkan penjelasan – SEKARANG 100% BERSIH ===
        findViewById<TextView>(R.id.txt2).text = CalculationFormatter.formatEnergyFormula(
            powerWatt = power,
            durationHour = durationHour,
            frequencyWeek = frequency
        )

        findViewById<TextView>(R.id.txt3).text = CalculationFormatter.formatWeeklyEnergy(energyPerWeek)

        findViewById<TextView>(R.id.txt6).text = CalculationFormatter.formatMonthlyCostFormula(energyPerWeek)

        findViewById<TextView>(R.id.txt7).text = CalculationFormatter.formatMonthlyCost(costPerMonth)
    }
}