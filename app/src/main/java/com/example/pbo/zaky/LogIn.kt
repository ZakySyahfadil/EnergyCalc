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

                    // Simpan LOGIN KEY
                    editor.putString("LOGIN_KEY", input)

                    // Ambil nama yang mungkin sudah pernah diubah user
                    val savedFirst = prefs.getString("firstname", null)
                    val savedLast = prefs.getString("lastname", null)

                    // Jika pertama login atau belum pernah ganti nama → simpan dari DB
                    if (savedFirst == null || savedLast == null) {
                        editor.putString("firstname", account.firstName)
                        editor.putString("lastname", account.lastName)
                    }

                    editor.apply()

                    // Pergi ke Welcome Page
                    val intent = Intent(this@LogIn, WelcomePage::class.java)
                    startActivity(intent)
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
