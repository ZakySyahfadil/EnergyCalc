package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.lifecycleScope
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import kotlinx.coroutines.launch

class LogIn : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)

        val inputEmail = findViewById<EditText>(R.id.inputEmail)
        val inputPass = findViewById<EditText>(R.id.inputPass)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnForgot = findViewById<Button>(R.id.btnForgot)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        val tvEmailError = findViewById<TextView>(R.id.tvEmailError)
        val tvPasswordError = findViewById<TextView>(R.id.tvPassError)
        val tvEmailRegist = findViewById<TextView>(R.id.tvEmailRegist)
        val tvPassIncorrect = findViewById<TextView>(R.id.tvPassIncorrect)
        val tvTelephoneRegist = findViewById<TextView>(R.id.tvTelephoneRegist)

        btnLogin.setOnClickListener {

            val input = inputEmail.text.toString().trim()
            val pass = inputPass.text.toString().trim()

            // Hide all errors
            tvEmailError.visibility = View.GONE
            tvPasswordError.visibility = View.GONE
            tvEmailRegist.visibility = View.GONE
            tvPassIncorrect.visibility = View.GONE
            tvTelephoneRegist.visibility = View.GONE

            if (input.isEmpty()) {
                tvEmailError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                tvPasswordError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@LogIn)
                val dao = db.accountDao()

                // Tentukan apakah input email atau nomor HP
                val isEmail = input.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                val isPhone = input.isDigitsOnly()

                val account = when {
                    isEmail -> dao.getAccountByEmail(input.lowercase())
                    isPhone -> dao.getAccountByPhone(input)
                    else -> dao.getAccountByEmail(input.lowercase()) // fallback untuk input campuran
                }

                // Tidak ditemukan
                if (account == null) {
                    runOnUiThread {
                        if (isEmail) {
                            tvEmailRegist.visibility = View.VISIBLE
                        } else if (isPhone) {
                            tvTelephoneRegist.visibility = View.VISIBLE
                        } else {
                            // Jika format tidak jelas → tampilkan error email
                            tvEmailRegist.visibility = View.VISIBLE
                        }
                    }
                    return@launch
                }

                // Password salah
                if (account.password != pass) {
                    runOnUiThread {
                        tvPassIncorrect.visibility = View.VISIBLE
                    }
                    return@launch
                }

                // LOGIN SUKSES
                runOnUiThread {
                    val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                    val editor = prefs.edit()

                    editor.putString("LOGIN_KEY", input)
                    editor.putString("firstname", account.firstName)
                    editor.putString("lastname", account.lastName)
                    editor.apply()

                    startActivity(Intent(this@LogIn, WelcomePage::class.java))
                    finish()
                }

            }
        }

        btnForgot.setOnClickListener {
            startActivity(Intent(this, EmailForgot::class.java))
        }

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
}