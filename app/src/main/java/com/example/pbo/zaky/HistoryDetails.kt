package com.example.pbo.zaky

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class HistoryDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_details)

        val id = intent.getIntExtra("history_id", -1)
        if (id == -1) {
            finish()
            return
        }

        val dao = AppDatabase.getDatabase(this).historyDao()

        // Views
        val txt1 = findViewById<TextView>(R.id.txt1)   // rumus dasar
        val txt2 = findViewById<TextView>(R.id.txt2)   // watt × hours
        val txt3 = findViewById<TextView>(R.id.txt3)   // hasil kWh / week
        val txt5 = findViewById<TextView>(R.id.txt5)   // kWh × tarif dasar
        val txt6 = findViewById<TextView>(R.id.txt6)   // perhitungan cost
        val txt7 = findViewById<TextView>(R.id.txt7)   // Rp cost / month

        val back = findViewById<ImageView>(R.id.bttnback)
        back.setOnClickListener { finish() }

        lifecycleScope.launch(Dispatchers.IO) {
            val item = dao.getHistoryById(id)
            if (item != null) {

                // --- PARSING VALUES ---
                val power = item.powerValue.replace("[^0-9]".toRegex(), "").toDoubleOrNull() ?: 0.0
                val duration = item.durationValue.replace("[^0-9]".toRegex(), "").toDoubleOrNull() ?: 0.0
                val frequency = item.frequencyValue.replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 0

                // convert minutes to hours
                val durationHours = duration / 60.0

                // --- CALCULATIONS (BENAR) ---
                val energyPerUse = (power / 1000.0) * durationHours
                val energyPerWeek = energyPerUse * frequency
                val energyPerMonth = energyPerWeek * 4
                val monthlyCost = energyPerMonth * 1500

                // Formatters
                val f2 = { v: Double -> String.format(Locale.getDefault(), "%.2f", v) }
                val f3 = { v: Double -> String.format(Locale.getDefault(), "%.3f", v) }
                val money = { v: Double -> String.format(Locale.getDefault(), "%,.0f", v) }

                withContext(Dispatchers.Main) {

                    // Rumus dasar
                    txt1.text = "= (watt × hours) ÷ 1000"

                    // watt × hours
                    txt2.text = "= (${power.toInt()} × ${f2(durationHours)}) ÷ 1000"

                    // kWh per week
                    txt3.text = "= ${f3(energyPerWeek)} kWh per week"

                    // rumus biaya
                    txt5.text = "= kWh × 1.500 × 4"

                    // kWh per week × tarif
                    txt6.text = "= ${f3(energyPerWeek)} × 1.500 × 4"

                    // hasil biaya
                    txt7.text = "Rp${money(monthlyCost)}/month"
                }
            }
        }
    }
}