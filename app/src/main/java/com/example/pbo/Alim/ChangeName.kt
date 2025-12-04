package com.example.pbo.Alim

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
import com.example.pbo.utils.DialogUtils
import kotlinx.coroutines.launch

class ChangeName : AppCompatActivity() {

    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var errorText: TextView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_name)

        firstName = findViewById(R.id.firstname)
        lastName = findViewById(R.id.lastname)
        errorText = findViewById(R.id.enteryourname)
        saveButton = findViewById(R.id.savebutton)

        errorText.visibility = View.GONE

        saveButton.setOnClickListener {
            if (validateName()) {
                DialogUtils.showUniversalDialog(
                    context = this,
                    message = "Are you sure you want to change your name?",
                    isConfirmation = true,
                    onConfirm = { performUpdateName() }
                )
            }
        }

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }
    }

    private fun validateName(): Boolean {
        val first = firstName.text.toString().trim()
        val last = lastName.text.toString().trim()

        return if (first.isEmpty() || last.isEmpty()) {
            errorText.visibility = View.VISIBLE
            false
        } else {
            errorText.visibility = View.GONE
            true
        }
    }

    private fun performUpdateName() {
        val first = firstName.text.toString().trim()
        val last = lastName.text.toString().trim()

        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val loginKey = prefs.getString("LOGIN_KEY", null)

        // Simpan ke SharedPreferences
        prefs.edit()
            .putString("firstname", first)
            .putString("lastname", last)
            .apply()

        // Hasil untuk SettingActivity (agar nama langsung ter-update tanpa logout)
        val returnIntent = Intent()
        returnIntent.putExtra("UPDATED_NAME", "$first $last")
        setResult(RESULT_OK, returnIntent)

        // Jika tidak ada LOGIN_KEY (misal dari SignUp langsung), tetap sukses
        if (loginKey == null) {
            DialogUtils.showUniversalDialog(
                context = this,
                message = "Your name has been updated.",
                isConfirmation = false,
                onDismiss = { finish() }
            )
            return
        }

        // Update database menggunakan coroutine
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@ChangeName)
            val dao = db.accountDao()

            dao.updateName(loginKey, first, last)

            runOnUiThread {
                DialogUtils.showUniversalDialog(
                    context = this@ChangeName,
                    message = "Your name has been updated.",
                    isConfirmation = false,
                    onDismiss = { finish() }
                )
            }
        }
    }
}
