package com.example.pbo.Nazwar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pbo.R
import com.google.android.material.button.MaterialButton

class Main_Page_Calculate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page_calculate)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toggle = findViewById<Switch>(R.id.switch_toggle)
        val warning = findViewById<LinearLayout>(R.id.text_view)

        toggle.setOnCheckedChangeListener { _, isChecked ->
            warning.visibility = if (isChecked) View.GONE else View.VISIBLE
        }

        val box1 = findViewById<EditText>(R.id.box1)
        val box2 = findViewById<EditText>(R.id.box2)
        val box3 = findViewById<EditText>(R.id.box3)
        val box4 = findViewById<EditText>(R.id.box4)

        val buttonCalculate = findViewById<MaterialButton>(R.id.button_calculate)

        buttonCalculate.setOnClickListener {

            // Ambil input
            val name = box1.text.toString().trim()
            val power = box2.text.toString().trim()
            val durationText = box3.text.toString().trim()
            val frequencyText = box4.text.toString().trim()

            var valid = true

            // Validasi
            if (name.isEmpty()) {
                box1.error = "Please enter your device name"
                valid = false
            }

            if (power.isEmpty()) {
                box2.error = "Please enter your device power"
                valid = false
            }

            if (!toggle.isChecked) {

                if (durationText.isEmpty() && frequencyText.isEmpty()) {
                    box3.error = "Please enter your weekly usage duration and frequency"
                    valid = false
                } else {
                    if (durationText.isEmpty()) {
                        box3.error = "Please enter your weekly usage duration"
                        valid = false
                    }
                    if (frequencyText.isEmpty()) {
                        box4.error = "Please enter your weekly usage frequency"
                        valid = false
                    }
                }
            }


            // Stop kalau ada error
            if (!valid) return@setOnClickListener

            // Hitung duration & frequency
            val isAlwaysOn = toggle.isChecked
            val duration: Int
            val frequency: Int

            if (isAlwaysOn) {
                duration = 168 * 60
                frequency = 1
            } else {
                duration = durationText.toInt()
                frequency = frequencyText.toInt()
            }

            // Pindah halaman
            val intent = Intent(this, Main_Page_Calculate_Results::class.java)
            intent.putExtra("deviceName", name)
            intent.putExtra("devicePower", power)
            intent.putExtra("duration", duration)
            intent.putExtra("frequency", frequency)
            startActivity(intent)
        }
    }
}
