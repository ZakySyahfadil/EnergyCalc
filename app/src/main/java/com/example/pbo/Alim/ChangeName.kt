package com.example.pbo.Alim

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
import com.example.pbo.utils.DialogUtils // Pastikan Import ini ada!
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

        // Tombol simpan
        saveButton.setOnClickListener {
            if (validateName()) {
                // Tampilkan Dialog Konfirmasi via DialogUtils
                DialogUtils.showUniversalDialog(
                    context = this,
                    message = "Are you sure you want to change your name?",
                    isConfirmation = true,
                    onConfirm = {
                        performUpdateName()
                    }
                )
            }
        }

        // Tombol Back
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

        // Update Database
        if (loginKey != null) {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@ChangeName)
                val dao = db.accountDao()
                dao.updateName(loginKey, first, last)

                // Setelah sukses update database, tampilkan Notif Sukses
                runOnUiThread {
                    DialogUtils.showUniversalDialog(
                        context = this@ChangeName,
                        message = "Your name has been updated.",
                        isConfirmation = false, // Mode Sukses (Tanpa tombol Yes/No)
                        onDismiss = {
                            finish() // Kembali ke menu sebelumnya saat dialog ditutup
                        }
                    )
                }
            }
        }
    }
}