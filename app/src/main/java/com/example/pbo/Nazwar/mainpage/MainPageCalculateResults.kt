package com.example.pbo.Nazwar.mainpage

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
import com.example.pbo.Nazwar.data.UserPreferences
import com.example.pbo.Nazwar.repository.HistoryRepositoryImpl
import com.example.pbo.Nazwar.usecase.SaveCalculationResultUseCase
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import com.example.pbo.utils.DialogUtils
import kotlinx.coroutines.launch

class MainPageCalculateResults : AppCompatActivity() {

    // Lazy initialization
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val userPrefs by lazy { UserPreferences(this) }
    private val historyRepo by lazy { HistoryRepositoryImpl(database.historyDao()) }
    private val saveUseCase by lazy { SaveCalculationResultUseCase(historyRepo, userPrefs) }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_main_page_calculate_result)

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            // === AMBIL DATA ===
            val name = intent.getStringExtra("deviceName") ?: "Unknown"
            val power = intent.getIntExtra("devicePower", 0)
            val duration = intent.getIntExtra("duration", 0)
            val frequency = intent.getIntExtra("frequency", 0)

            // === UI ===
            findViewById<TextView>(R.id.device).text = name
            findViewById<TextView>(R.id.AmtPower).text = getString(R.string.label_power, power)
            findViewById<TextView>(R.id.AmtDuration).text =
                getString(R.string.label_duration, duration)
            findViewById<TextView>(R.id.AmtFrequency).text =
                getString(R.string.label_frequency, frequency)

            // === HITUNG BIAYA ===
            val powerWatt = power.toDouble()
            val durationMin = duration.toDouble()
            val frequencyWeek = frequency.toDouble()
            val tariff = 1500.0

            val energyPerUse = (powerWatt / 1000.0) * (durationMin / 60.0)
            val energyPerWeek = energyPerUse * frequencyWeek
            val energyPerMonth = energyPerWeek * 4.0
            val costPerMonth = energyPerMonth * tariff

            findViewById<TextView>(R.id.amount).text = "Rp${"%,.0f".format(costPerMonth)}/month"

            // === TOMBOL KEMBALI ===
            findViewById<ImageView>(R.id.panah).setOnClickListener {
                startActivity(Intent(this, MainPage::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                })
                finish()
            }

            // === TOMBOL DETAIL ===
            findViewById<Button>(R.id.btn_detail).setOnClickListener {
                startActivity(Intent(this, MainPageCalculateDetails::class.java).apply {
                    putExtra("power", power)
                    putExtra("duration", duration)
                    putExtra("frequency", frequency)
                    putExtra("costPerMonth", costPerMonth)
                    putExtra("energyPerWeek", energyPerWeek)
                })
            }

            // === TOMBOL SAVE ===
            findViewById<Button>(R.id.btn_save).setOnClickListener {
                DialogUtils.showUniversalDialog(
                    context = this,
                    message = "Are you sure you want to save this result?",
                    isConfirmation = true,
                    onConfirm = {
                        lifecycleScope.launch {
                            val success = saveUseCase(
                                deviceName = name,
                                powerWatt = power,
                                durationMin = duration,
                                frequencyWeek = frequency,
                                energyPerMonth = energyPerMonth,
                                costPerMonth = costPerMonth
                            )

                            val message = if (success) {
                                "Result saved successfully! You can see it in History."
                            } else {
                                "Failed to save. Please login again."
                            }

                            DialogUtils.showUniversalDialog(
                                context = this@MainPageCalculateResults,
                                message = message,
                                isConfirmation = false
                            )
                        }
                    }
                )
            }
    }
}