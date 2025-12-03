package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

            // Sembunyikan semua error
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

                // Cari user berdasarkan email atau nomor telepon
                val account = if (input.contains("@")) {
                    dao.getAccountByEmail(input.lowercase())
                } else {
                    dao.getAccountByPhone(input)
                }

                if (account == null) {
                    runOnUiThread {
                        if (input.contains("@")) {
                            tvEmailRegist.visibility = View.VISIBLE
                        } else {
                            tvTelephoneRegist.visibility = View.VISIBLE
                        }
                    }
                    return@launch
                }

                if (account.password != pass) {
                    runOnUiThread {
                        tvPassIncorrect.visibility = View.VISIBLE
                    }
                    return@launch
                }

                // ✔ LOGIN SUKSES
                runOnUiThread {
                    val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                    val editor = prefs.edit()

                    // Simpan login key (email/no hp)
                    editor.putString("LOGIN_KEY", input)

                    // Ambil data NAMA TERBARU dari database (bukan SharedPreferences)
                    editor.putString("firstname", account.firstName)
                    editor.putString("lastname", account.lastName)

                    editor.apply()

                    startActivity(Intent(this@LogIn, WelcomePage::class.java))
                    finish()
                }

            }
        }

        // Forgot Password
        btnForgot.setOnClickListener {
            startActivity(Intent(this, EmailForgot::class.java))
        }

        // Sign Up
        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
}
