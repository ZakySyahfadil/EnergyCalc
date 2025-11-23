package com.example.pbo.Alim

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.pbo.R
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class changeName : AppCompatActivity() {

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

        // ✔ Listener hanya satu kali
        saveButton.setOnClickListener {
            if (validateName()) {
                showConfirmDialog()
            }
        }


        val btn_back = findViewById<ImageView>(R.id.btn_back)
        btn_back.setOnClickListener {
            val intent = Intent(this, settingActivity::class.java)
            startActivity(intent)
        }
    }

    // ✔ sekarang mengembalikan Boolean
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
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnYes = dialogView.findViewById<Button>(R.id.btnYes)
        val btnNo = dialogView.findViewById<Button>(R.id.btnNo)

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        btnYes.setOnClickListener {
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
            .setCancelable(true)   // ⬅ bisa ditekan di mana saja
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        // Ketika dialog ditutup → kembali ke halaman sebelumnya
        dialog.setOnDismissListener {
            finish()  // kembali ke activity sebelumnya
        }
    }
}
