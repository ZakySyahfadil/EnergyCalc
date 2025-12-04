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
import com.example.pbo.utils.DialogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        // --- AMBIL DATA ---
        val name = intent.getStringExtra("deviceName")
        val power = intent.getStringExtra("devicePower")
        val duration = intent.getIntExtra("duration", 0)
        val frequency = intent.getIntExtra("frequency", 0)

        // --- TOMBOL BACK ---
        val buttonBack = findViewById<ImageView>(R.id.panah)
        buttonBack.setOnClickListener { finish() }

        // --- TAMPILKAN UI ---
        findViewById<TextView>(R.id.device).text = name
        findViewById<TextView>(R.id.AmtPower).text = getString(R.string.label_power, power)
        findViewById<TextView>(R.id.AmtDuration).text = getString(R.string.label_duration, duration)
        findViewById<TextView>(R.id.AmtFrequency).text = getString(R.string.label_frequency, frequency)

        // --- PERHITUNGAN ---
        val powerWatt = power?.toDoubleOrNull() ?: 0.0
        val durationMin = duration.toDouble()
        val frequencyWeek = frequency.toDouble()
        val tariff = 1500.0

        val energyPerUse = (powerWatt / 1000.0) * (durationMin / 60.0)
        val energyPerWeek = energyPerUse * frequencyWeek
        val energyPerMonth = energyPerWeek * 4.0

        val costPerMonth = energyPerMonth * tariff

        // Tampilkan biaya
        val amountText = findViewById<TextView>(R.id.amount)
        amountText.text = "Rp${"%,.0f".format(costPerMonth)}/month"

        // --- TOMBOL DETAIL ---
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
        //     LOGIKA SAVE DENGAN NOTIFIKASI
        // ----------------------------------------------------------
        val btnSave = findViewById<Button>(R.id.btn_save)
        btnSave.setOnClickListener {

            // 1. Tampilkan Dialog Konfirmasi Dulu
            DialogUtils.showUniversalDialog(
                context = this,
                message = "Are you sure you want to save this result?", // Pesan Konfirmasi
                isConfirmation = true,
                onConfirm = {
                    // 2. Jika user pilih YES, jalankan penyimpanan
                    performSaveToDatabase(
                        name = name,
                        energyPerMonth = energyPerMonth,
                        costPerMonth = costPerMonth,
                        power = power,
                        duration = duration,
                        frequency = frequency
                    )
                }
            )
        }
    }

    // Fungsi untuk menyimpan ke database (Dipisah biar rapi)
    private fun performSaveToDatabase(
        name: String?,
        energyPerMonth: Double,
        costPerMonth: Double,
        power: String?,
        duration: Int,
        frequency: Int
    ) {
        val currentDateString = currentDate()
        val powerStr = "$power W"
        val durationStr = "$duration minutes"
        val freqStr = "$frequency times"

        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@MainPageCalculateResults)

            // Insert data
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

            // 3. Setelah sukses simpan, Kembali ke Main Thread untuk tampilkan Dialog Sukses
            withContext(Dispatchers.Main) {
                DialogUtils.showUniversalDialog(
                    context = this@MainPageCalculateResults,
                    message = "Result saved. You can view it in History.", // Pesan Sukses
                    isConfirmation = false,
                    onDismiss = {
                        // Opsional: Tutup halaman atau disable tombol save jika mau
                        // finish()
                    }
                )
            }
        }
    }

    private fun currentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}