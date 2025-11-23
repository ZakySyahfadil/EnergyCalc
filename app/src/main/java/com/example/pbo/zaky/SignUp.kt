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
import androidx.lifecycle.lifecycleScope
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import kotlinx.coroutines.launch

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

            tvEmpty.visibility = TextView.GONE
            tvFormat.visibility = TextView.GONE
            tvUsed.visibility = TextView.GONE

            var valid = true

            if (userInput.isEmpty()) {
                tvEmpty.visibility = TextView.VISIBLE
                valid = false
            }
            else if (!isValidEmail(userInput) && !isValidPhone(userInput)) {
                tvFormat.text = "Format must be a valid email or phone number"
                tvFormat.visibility = TextView.VISIBLE
                valid = false
            }

            if (valid) {
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(this@SignUp)
                    val dao = db.accountDao()

                    val emailExists = dao.getAccountByEmail(userInput)
                    val phoneExists = dao.getAccountByPhone(userInput)

                    if (emailExists != null || phoneExists != null) {
                        tvUsed.text = "Email / phone number already registered"
                        tvUsed.visibility = TextView.VISIBLE
                    } else {
                        val intent = Intent(this@SignUp, SignUp2::class.java)
                        intent.putExtra("identifier", userInput)
                        startActivity(intent)
                    }
                }
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