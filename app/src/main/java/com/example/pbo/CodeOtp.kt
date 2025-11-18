package com.example.pbo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CodeOtp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_code_otp)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        val tvCodeError = findViewById<TextView>(R.id.tvCodeError)
        val Continue = findViewById<Button>(R.id.Continue)

        // Ambil EditText
        val otp1 = findViewById<EditText>(R.id.otp1)
        val otp2 = findViewById<EditText>(R.id.otp2)
        val otp3 = findViewById<EditText>(R.id.otp3)
        val otp4 = findViewById<EditText>(R.id.otp4)
        val otp5 = findViewById<EditText>(R.id.otp5)
        val otp6 = findViewById<EditText>(R.id.otp6)

        val otpBoxes = arrayOf(otp1, otp2, otp3, otp4, otp5, otp6)

        // AUTO MOVE & AUTO BACKSPACE (VERSI SIMPLE)
        for (i in otpBoxes.indices) {
            otpBoxes[i].addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {
                    // Jika user isi 1 angka → pindah ke kotak berikutnya
                    if (s?.length == 1 && i < otpBoxes.size - 1) {
                        otpBoxes[i + 1].requestFocus()
                    }

                    // Jika user hapus angka → kembali ke kotak sebelumnya
                    else if (s?.isEmpty() == true && i > 0) {
                        otpBoxes[i - 1].requestFocus()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        Continue.setOnClickListener {

            // Ambil OTP
            val otpCode = otp1.text.toString() +
                    otp2.text.toString() +
                    otp3.text.toString() +
                    otp4.text.toString() +
                    otp5.text.toString() +
                    otp6.text.toString()

            tvCodeError.visibility = View.GONE

            // Jika ada OTP kosong → tampilkan error
            if (otpCode.length < 6) {
                tvCodeError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            // HILANGKAN ERROR KETIKA USER MULAI MENGETIK
            for (box in otpBoxes) {
                box.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        tvCodeError.visibility = View.GONE
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
            }
        }
    }
}
