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
import com.example.pbo.data.repository.AccountRepository
import com.example.pbo.domain.LoginResult
import com.example.pbo.domain.LoginUseCase
import kotlinx.coroutines.launch

class LogIn : AppCompatActivity() {

    private lateinit var loginUseCase: LoginUseCase

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

        // Init use case
        val dao = AppDatabase.getDatabase(this).accountDao()
        val repo = AccountRepository(dao)
        loginUseCase = LoginUseCase(repo)

        btnLogin.setOnClickListener {

            val input = inputEmail.text.toString().trim()
            val pass = inputPass.text.toString().trim()

            // Hide all errors
            tvEmailError.visibility = View.GONE
            tvPasswordError.visibility = View.GONE
            tvEmailRegist.visibility = View.GONE
            tvPassIncorrect.visibility = View.GONE
            tvTelephoneRegist.visibility = View.GONE

            lifecycleScope.launch {

                when (val result = loginUseCase.execute(input, pass)) {

                    LoginResult.InputEmpty ->
                        tvEmailError.visibility = View.VISIBLE

                    LoginResult.PasswordEmpty ->
                        tvPasswordError.visibility = View.VISIBLE

                    LoginResult.NotRegistered -> {
                        if (input.contains("@"))
                            tvEmailRegist.visibility = View.VISIBLE
                        else if (input.all { it.isDigit() })
                            tvTelephoneRegist.visibility = View.VISIBLE
                        else
                            tvEmailRegist.visibility = View.VISIBLE
                    }

                    LoginResult.WrongPassword ->
                        tvPassIncorrect.visibility = View.VISIBLE

                    is LoginResult.Success -> {
                        val account = result.account
                        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                        prefs.edit()
                            .putString("LOGIN_KEY", input)
                            .putString("firstname", account.firstName)
                            .putString("lastname", account.lastName)
                            .apply()

                        startActivity(Intent(this@LogIn, WelcomePage::class.java))
                        finish()
                    }
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