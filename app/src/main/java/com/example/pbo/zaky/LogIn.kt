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

        btnLogin.setOnClickListener {

            val email = inputEmail.text.toString().trim().lowercase()
            val pass = inputPass.text.toString().trim()

            tvEmailError.visibility = View.GONE
            tvPasswordError.visibility = View.GONE

            // Validasi input kosong
            if (email.isEmpty()) {
                tvEmailError.text = "Please enter your email."
                tvEmailError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                tvPasswordError.text = "Please enter your password."
                tvPasswordError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // 🔥 CEK EMAIL & PASSWORD LEWAT ROOM
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@LogIn)
                val dao = db.accountDao()

                val account = dao.getAccountByEmail(email)

                if (account == null) {
                    runOnUiThread {
                        tvEmailError.text = "Account not found."
                        tvEmailError.visibility = View.VISIBLE
                    }
                    return@launch
                }

                if (account.password != pass) {
                    runOnUiThread {
                        tvPasswordError.text = "Incorrect password."
                        tvPasswordError.visibility = View.VISIBLE
                    }
                    return@launch
                }

                // Jika login berhasil → masuk ke WelcomePage
                runOnUiThread {
                    val intent = Intent(this@LogIn, WelcomePage::class.java)
                    intent.putExtra("USER_NAME", "${account.firstName} ${account.lastName}")
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