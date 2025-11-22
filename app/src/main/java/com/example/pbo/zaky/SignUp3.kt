package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
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

        // Ambil email dari halaman SignUp2
        val email = intent.getStringExtra("identifier")

        // Tombol Back
        btnBack.setOnClickListener {
            finish()
        }

        sendCodeBtn.setOnClickListener {

            val first = firstName.text.toString().trim()
            val last = lastName.text.toString().trim()
            val pass = password.text.toString().trim()

            var valid = true

            // Cek nama kosong
            if (first.isEmpty() || last.isEmpty()) {
                failName.visibility = TextView.VISIBLE
                valid = false
            } else {
                failName.visibility = TextView.GONE
            }

            // Cek password kosong
            if (pass.isEmpty()) {
                failPass.visibility = TextView.VISIBLE
                passCondition.visibility = TextView.GONE
                valid = false
            } else {
                failPass.visibility = TextView.GONE
            }

            // Validasi syarat password
            if (pass.isNotEmpty() && !isPasswordValid(pass)) {
                passCondition.visibility = TextView.VISIBLE
                valid = false
            } else {
                passCondition.visibility = TextView.GONE
            }

            // Email harus ada
            if (email.isNullOrEmpty()) {
                Toast.makeText(this, "Email hilang. Ulangi proses Sign Up.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (valid) if (valid) {
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(this@SignUp3)
                    val dao = db.accountDao()

                    // INSERT ke Room
                    dao.insertAccount(Account(email = email, password = pass))

                    // CEK HASILNYA di LOG
                    val acc = dao.getAccountByEmail(email)
                    android.util.Log.d("DB_TEST", "HASIL: $acc")

                    runOnUiThread {
                        val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
                        sharedPref.edit().apply {
                            putString("firstname", first)
                            putString("lastname", last)
                            putString("email", email)
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

    // Fungsi Validasi Password
    private fun isPasswordValid(pass: String): Boolean {
        val hasNumber = pass.any { it.isDigit() }
        val hasLetters = pass.count { it.isLetter() } >= 7
        return hasNumber && hasLetters
    }
}