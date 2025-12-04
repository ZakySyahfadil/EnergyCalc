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
import com.example.pbo.R
import com.google.android.material.button.MaterialButton

class MainPageCalculate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page_calculate)

        // Padding system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toggle = findViewById<Switch>(R.id.switch_toggle)
        val extraFields = findViewById<LinearLayout>(R.id.text_view)

        val box1 = findViewById<EditText>(R.id.box1)
        val box2 = findViewById<EditText>(R.id.box2)
        val box3 = findViewById<EditText>(R.id.box3)
        val box4 = findViewById<EditText>(R.id.box4)

        val wrong1 = findViewById<TextView>(R.id.wrong1)
        val wrong2 = findViewById<TextView>(R.id.wrong2)
        val wrong3 = findViewById<TextView>(R.id.wrong3) // ← perbaikan ID di sini

        val buttonBack = findViewById<ImageView>(R.id.panah)
        buttonBack.setOnClickListener { finish() }

        toggle.setOnCheckedChangeListener { _, isChecked ->
            extraFields.visibility = if (isChecked) View.GONE else View.VISIBLE
            wrong3.visibility = View.GONE // sembunyikan weekly error saat toggle berubah
        }

        val buttonCalculate = findViewById<MaterialButton>(R.id.button_calculate)
        buttonCalculate.setOnClickListener {

            val name = box1.text.toString().trim()
            val power = box2.text.toString().trim()
            val durationText = box3.text.toString().trim()
            val frequencyText = box4.text.toString().trim()

            var valid = true
            // Reset semua error
            wrong1.visibility = View.GONE
            wrong2.visibility = View.GONE
            wrong3.visibility = View.GONE

            // Validasi device name
            if (name.isEmpty()) {
                wrong1.visibility = View.VISIBLE
                valid = false
            }

            // Validasi device power
            if (power.isEmpty()) {
                wrong2.visibility = View.VISIBLE
                valid = false
            }

            // Validasi duration & frequency saat switch OFF
            if (!toggle.isChecked) {
                when {
                    durationText.isEmpty() && frequencyText.isEmpty() -> {
                        wrong3.text = "Please enter your weekly duration and frequency"
                        wrong3.visibility = View.VISIBLE
                        valid = false
                    }
                    durationText.isEmpty() -> {
                        wrong3.text = "Please enter your weekly duration"
                        wrong3.visibility = View.VISIBLE
                        valid = false
                    }
                    frequencyText.isEmpty() -> {
                        wrong3.text = "Please enter your weekly frequency"
                        wrong3.visibility = View.VISIBLE
                        valid = false
                    }
                }
            }

            if (!valid) return@setOnClickListener

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

            val intent = Intent(this, MainPageCalculateResults::class.java)
            intent.putExtra("deviceName", name)
            intent.putExtra("devicePower", power)
            intent.putExtra("duration", duration)
            intent.putExtra("frequency", frequency)

            startActivity(intent)
        }
    }
}