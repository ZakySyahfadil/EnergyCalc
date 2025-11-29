package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R
import com.example.pbo.data.Account
import com.example.pbo.data.AppDatabase
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SignUp3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up3)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val firstName = findViewById<EditText>(R.id.firstname)
        val lastName = findViewById<EditText>(R.id.lastname)
        val password = findViewById<EditText>(R.id.passsign)

        val failName = findViewById<TextView>(R.id.failfirst)
        val failPass = findViewById<TextView>(R.id.failpass)
        val passCondition = findViewById<TextView>(R.id.PassCondition)

        val sendCodeBtn = findViewById<Button>(R.id.newsign)

        // Ambil identifier dari SignUp2 (email ATAU phone)
        val identifier = intent.getStringExtra("identifier")

        btnBack.setOnClickListener {
            finish()
        }

        sendCodeBtn.setOnClickListener {

            val first = firstName.text.toString().trim()
            val last = lastName.text.toString().trim()
            val pass = password.text.toString().trim()

            var valid = true

            if (first.isEmpty() || last.isEmpty()) {
                failName.visibility = TextView.VISIBLE
                valid = false
            } else {
                failName.visibility = TextView.GONE
            }

            if (pass.isEmpty()) {
                failPass.visibility = TextView.VISIBLE
                passCondition.visibility = TextView.GONE
                valid = false
            } else {
                failPass.visibility = TextView.GONE
            }

            if (pass.isNotEmpty() && !isPasswordValid(pass)) {
                passCondition.visibility = TextView.VISIBLE
                valid = false
            } else {
                passCondition.visibility = TextView.GONE
            }

            if (identifier.isNullOrEmpty()) {
                Toast.makeText(this, "Identifier hilang. Ulangi Sign Up.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (valid) {
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(this@SignUp3)
                    val dao = db.accountDao()

                    // Tentukan apakah identifier ini email atau nomor HP
                    val isEmail = Patterns.EMAIL_ADDRESS.matcher(identifier).matches()

                    val account = Account(
                        firstName = first,
                        lastName = last,
                        email = if (isEmail) identifier else "",
                        phoneNumber = if (!isEmail) identifier else "",
                        password = pass
                    )

                    dao.insertAccount(account)

                    runOnUiThread {
                        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
                        sharedPref.edit().apply {
                            putString("firstname", first)
                            putString("lastname", last)
                            putString("email", if (isEmail) identifier else "")
                            putString("phone", if (!isEmail) identifier else "")
                            apply()
                        }

                        val fullName = "$first $last"
                        val intent = Intent(this@SignUp3, WelcomePage::class.java)
                        intent.putExtra("USER_NAME", fullName)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun isPasswordValid(pass: String): Boolean {
        val hasNumber = pass.any { it.isDigit() }
        val hasLetters = pass.count { it.isLetter() } >= 7
        return hasNumber && hasLetters
    }
}