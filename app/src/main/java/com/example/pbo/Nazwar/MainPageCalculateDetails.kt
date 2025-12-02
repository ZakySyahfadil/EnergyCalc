package com.example.pbo.Nazwar

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        val power = intent.getStringExtra("power")?.toDoubleOrNull() ?: 0.0
        val duration = intent.getStringExtra("duration")?.toDoubleOrNull() ?: 0.0
        val frequency = intent.getStringExtra("frequency")?.toDoubleOrNull() ?: 0.0

        val energyPerWeek = intent.getDoubleExtra("energyPerWeek", 0.0)
        val costPerMonth = intent.getDoubleExtra("costPerMonth", 0.0)


        val txt2 = findViewById<TextView>(R.id.txt2)
        txt2.text = "= (${power.toInt()} × ${"%.2f".format(duration/60 * frequency)}) ÷ 1000"

        val txt3 = findViewById<TextView>(R.id.txt3)
        txt3.text = "= ${"%.3f".format(energyPerWeek)} kWh per week"

        val txt5 = findViewById<TextView>(R.id.txt5)
        txt5.text = "= ${"%.3f".format(energyPerWeek)} × 1.500 × 4"

        val txt6 = findViewById<TextView>(R.id.txt6)
        txt6.text = "= ${"%.3f".format(energyPerWeek)} × 1.500 × 4"

        val txt7 = findViewById<TextView>(R.id.txt7)
        txt7.text = "Rp${"%,.0f".format(costPerMonth)}/month"

    }
}