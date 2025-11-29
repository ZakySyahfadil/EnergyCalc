package com.example.pbo.Alim

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pbo.R
import com.example.pbo.data.AppDatabase
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
                showConfirmDialog()
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

    private fun showConfirmDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_confirm, null)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        val btnYes = view.findViewById<Button>(R.id.btnYes)
        val btnNo = view.findViewById<Button>(R.id.btnNo)

        // Batalkan
        btnNo.setOnClickListener { dialog.dismiss() }

        // Konfirmasi
        btnYes.setOnClickListener {
            val first = firstName.text.toString().trim()
            val last = lastName.text.toString().trim()

            val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
            val loginKey = prefs.getString("LOGIN_KEY", null)

            // Simpan ke SharedPreferences
            prefs.edit()
                .putString("firstname", first)
                .putString("lastname", last)
                .apply()

            // 🔥 UPDATE NAMA DI DATABASE
            if (loginKey != null) {
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(this@ChangeName)
                    val dao = db.accountDao()
                    dao.updateName(loginKey, first, last)
                }
            }

            dialog.dismiss()
            showSuccessDialog()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun showSuccessDialog() {
        val view = layoutInflater.inflate(R.layout.notif_success, null)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(true)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        dialog.setOnDismissListener { finish() }
    }
}
