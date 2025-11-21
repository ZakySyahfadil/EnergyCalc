package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R
import com.example.pbo.zaky.SignUp
import com.example.pbo.zaky.WelcomePage
import com.example.pbo.utils.ILoginController
import com.example.pbo.utils.LoginController

class LogIn : AppCompatActivity() {

    private val loginController: ILoginController = LoginController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)

        val inputEmail = findViewById<EditText>(R.id.inputEmail)
        val inputPass = findViewById<EditText>(R.id.inputPass)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnForgot = findViewById<Button>(R.id.btnForgot)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        // Error text
        val tvEmailError = findViewById<TextView>(R.id.tvEmailError)
        val tvPasswordError = findViewById<TextView>(R.id.tvPassError)

        btnLogin.setOnClickListener {

            // Ambil input dan normalisasi sedikit (trim + lowercase untuk perbandingan email)
            val emailInputRaw = inputEmail.text.toString()
            val email = emailInputRaw.trim()
            val emailNormalized = email.lowercase()
            val pass = inputPass.text.toString().trim()

            // Reset error visibility
            tvEmailError.visibility = View.GONE
            tvPasswordError.visibility = View.GONE

            var valid = true

            // Validasi email kosong
            if (email.isEmpty()) {
                tvEmailError.text = "Please enter your email."
                tvEmailError.visibility = View.VISIBLE
                valid = false
            }

            // Validasi password kosong
            if (pass.isEmpty()) {
                tvPasswordError.text = "Please enter your password."
                tvPasswordError.visibility = View.VISIBLE
                valid = false
            }

            if (!valid) return@setOnClickListener

            // Ambil SharedPreferences yang kita gunakan di SignUp
            val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)

            // 1) Ambil email yang disimpan di key "email" (single-account style)
            val savedEmailRaw = sharedPref.getString("email", null)
            val savedEmail = savedEmailRaw?.trim()?.lowercase()

            // 2) Ambil set USED_EMAILS (jika ada) — ini berguna jika SignUp menyimpan daftar
            val savedEmailSet = sharedPref.getStringSet("USED_EMAILS", emptySet()) ?: emptySet()
            // normalisasi set untuk perbandingan case-insensitive
            val savedEmailSetNormalized = savedEmailSet.map { it.trim().lowercase() }.toSet()

            // 3) Ambil password yang tersimpan (hanya single-account style)
            val savedPassword = sharedPref.getString("password", null)

            // =========================
            //   CEK APAKAH EMAIL TERDAFTAR
            // =========================
            val emailIsRegistered = when {
                // jika single stored email cocok
                savedEmail != null && emailNormalized == savedEmail -> true
                // atau jika email ada di set USED_EMAILS
                savedEmailSetNormalized.contains(emailNormalized) -> true
                else -> false
            }

            if (!emailIsRegistered) {
                tvEmailError.text = "Email not registered"
                tvEmailError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // =========================
            //   CEK PASSWORD
            // =========================
            // Jika kita menyimpan password di key "password" maka bandingkan.
            // Jika savedPassword == null (misalnya password tidak pernah diset di SignUp3),
            // kita anggap password salah.
            if (savedPassword == null || pass != savedPassword) {
                tvPasswordError.text = "Incorrect password, please try again."
                tvPasswordError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Jika login berhasil
            val savedFirstname = sharedPref.getString("firstname", "") ?: ""
            val savedLastname = sharedPref.getString("lastname", "") ?: ""
            val fullName = "$savedFirstname $savedLastname"

            val intent = Intent(this, WelcomePage::class.java)
            intent.putExtra("USER_NAME", fullName)
            startActivity(intent)
            finish()
        }

        btnForgot.setOnClickListener {
            startActivity(Intent(this, EmailForgot::class.java))
        }

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
}