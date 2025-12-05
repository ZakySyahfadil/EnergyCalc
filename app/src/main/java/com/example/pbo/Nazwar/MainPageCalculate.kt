package com.example.pbo.Nazwar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pbo.Nazwar.calculation.AlwaysOnCalculator
import com.example.pbo.Nazwar.calculation.WeeklyCalculator
import com.example.pbo.Nazwar.model.DeviceUsage
import com.example.pbo.Nazwar.validation.InputValidator
import com.example.pbo.R
import com.google.android.material.button.MaterialButton

class MainPageCalculate : AppCompatActivity() {

    private lateinit var validator: InputValidator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page_calculate)

        validator = InputValidator()

        // UI Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // UI Components
        val toggle = findViewById<Switch>(R.id.switch_toggle)
        val extraFields = findViewById<LinearLayout>(R.id.text_view)

        val boxName = findViewById<EditText>(R.id.box1)
        val boxPower = findViewById<EditText>(R.id.box2)
        val boxDuration = findViewById<EditText>(R.id.box3)
        val boxFrequency = findViewById<EditText>(R.id.box4)

        val wrong1 = findViewById<TextView>(R.id.wrong1)
        val wrong2 = findViewById<TextView>(R.id.wrong2)
        val wrong3 = findViewById<TextView>(R.id.wrong3)

        val btnBack = findViewById<ImageView>(R.id.panah)
        btnBack.setOnClickListener { finish() }

        // Toggle always-on
        toggle.setOnCheckedChangeListener { _, isChecked ->
            extraFields.visibility = if (isChecked) View.GONE else View.VISIBLE
            wrong3.visibility = View.GONE
        }

        // Calculate button
        val buttonCalculate = findViewById<MaterialButton>(R.id.button_calculate)
        buttonCalculate.setOnClickListener {
            val name = boxName.text.toString().trim()
            val power = boxPower.text.toString().trim()
            val durationText = boxDuration.text.toString().trim()
            val frequencyText = boxFrequency.text.toString().trim()

            // Reset semua error
            wrong1.visibility = View.GONE
            wrong2.visibility = View.GONE
            wrong3.visibility = View.GONE

            val result = validator.validate(name, power, durationText, frequencyText, toggle.isChecked)

            // Tampilkan semua error yang ada
            if (result.nameError) wrong1.visibility = View.VISIBLE
            if (result.powerError) wrong2.visibility = View.VISIBLE

            if (result.bothDurationFrequencyError) {
                wrong3.text = "Please enter your weekly duration and frequency"
                wrong3.visibility = View.VISIBLE
            } else if (result.durationError) {
                wrong3.text = "Please enter your weekly duration"
                wrong3.visibility = View.VISIBLE
            } else if (result.frequencyError) {
                wrong3.text = "Please enter your weekly frequency"
                wrong3.visibility = View.VISIBLE
            }

            // Jika ada error, stop di sini
            if (!result.isValid) {
                return@setOnClickListener
            }

            // Lanjut ke perhitungan...
            val calculator = if (toggle.isChecked) AlwaysOnCalculator() else WeeklyCalculator()
            val (duration, frequency) = calculator.calculate(durationText, frequencyText)

            val device = DeviceUsage(name, power.toInt(), duration, frequency)

            val intent = Intent(this, MainPageCalculateResults::class.java).apply {
                putExtra("deviceName", device.name)
                putExtra("devicePower", device.power)
                putExtra("duration", device.duration)
                putExtra("frequency", device.frequency)
            }
            startActivity(intent)
        }
    }
}