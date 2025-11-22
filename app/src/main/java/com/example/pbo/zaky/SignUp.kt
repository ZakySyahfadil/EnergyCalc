package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        // UI components
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val input = findViewById<EditText>(R.id.emailedit)

        val tvEmpty = findViewById<TextView>(R.id.signfail)
        val tvFormat = findViewById<TextView>(R.id.emailformat)
        val tvUsed = findViewById<TextView>(R.id.emailused)

        val btnSendCode = findViewById<Button>(R.id.buttonSendCode)

        btnBack.setOnClickListener { finish() }

        btnSendCode.setOnClickListener {
            val userInput = input.text.toString().trim()

            // RESET error messages
            tvEmpty.visibility = TextView.GONE
            tvFormat.visibility = TextView.GONE
            tvUsed.visibility = TextView.GONE

            var valid = true

            // 1️⃣ Tidak boleh kosong
            if (userInput.isEmpty()) {
                tvEmpty.visibility = TextView.VISIBLE
                valid = false

                // 2️⃣ Format harus email ATAU nomor HP
            } else if (!isValidEmail(userInput) && !isValidPhone(userInput)) {
                tvFormat.text = "Format must be a valid email or phone number"
                tvFormat.visibility = TextView.VISIBLE
                valid = false
            }

            if (valid) {
                // Lanjut ke halaman OTP / SignUp2, kirim identifier (email atau phone)
                val intent = Intent(this, SignUp2::class.java)
                intent.putExtra("identifier", userInput)
                startActivity(intent)
            }
        }
    }

    // Cek format email
    private fun isValidEmail(input: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    // Cek format nomor HP Indonesia (08xxxxxxxx...)
    private fun isValidPhone(input: String): Boolean {
        // terima 08 di depan dan total panjang 10..14 digit (sesuaikan bila mau)
        return input.matches(Regex("^08[0-9]{8,12}$"))
    }
}