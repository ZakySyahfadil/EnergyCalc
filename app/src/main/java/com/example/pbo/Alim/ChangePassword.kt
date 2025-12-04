package com.example.pbo.Alim

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
import com.example.pbo.data.Account
import com.example.pbo.utils.DialogUtils
import com.example.pbo.zaky.ResetPass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePassword : AppCompatActivity() {

    private lateinit var etOldPass: EditText
    private lateinit var etNewPass: EditText
    private lateinit var etConfirmPass: EditText

    private lateinit var tvErrorEmptyOld: TextView
    private lateinit var tvErrorWrongOld: TextView
    private lateinit var tvErrorEmptyNew: TextView
    private lateinit var tvErrorEmptyConfirm: TextView
    private lateinit var tvErrorMismatch: TextView

    private lateinit var btnSave: Button
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_password)

        initView()

        btnSave.setOnClickListener { handleSavePassword() }
        btnBack.setOnClickListener { finish() }
        findViewById<Button>(R.id.btnForgot).setOnClickListener {
            val intent = Intent(this, ResetPass::class.java)
            startActivity(intent)
        }

    }

    private fun initView() {
        etOldPass = findViewById(R.id.enteryouroldpass)
        etNewPass = findViewById(R.id.enteryournewpass)
        etConfirmPass = findViewById(R.id.confirmyournewpass)

        tvErrorEmptyOld = findViewById(R.id.wrong1)
        tvErrorWrongOld = findViewById(R.id.wrong4)
        tvErrorEmptyNew = findViewById(R.id.wrong2)
        tvErrorEmptyConfirm = findViewById(R.id.wrong3)
        tvErrorMismatch = findViewById(R.id.wrong5)

        btnSave = findViewById(R.id.btnLogin)
        btnBack = findViewById(R.id.btn_back)

        hideAllErrors()
    }

    private fun hideAllErrors() {
        tvErrorEmptyOld.visibility = View.GONE
        tvErrorWrongOld.visibility = View.GONE
        tvErrorEmptyNew.visibility = View.GONE
        tvErrorEmptyConfirm.visibility = View.GONE
        tvErrorMismatch.visibility = View.GONE
    }

    private fun handleSavePassword() {
        hideAllErrors()

        val oldPassInput = etOldPass.text.toString().trim()
        val newPassInput = etNewPass.text.toString().trim()
        val confirmPassInput = etConfirmPass.text.toString().trim()

        var isValid = true

        if (oldPassInput.isEmpty()) {
            tvErrorEmptyOld.visibility = View.VISIBLE
            isValid = false
        }
        if (newPassInput.isEmpty()) {
            tvErrorEmptyNew.visibility = View.VISIBLE
            isValid = false
        }
        if (confirmPassInput.isEmpty()) {
            tvErrorEmptyConfirm.visibility = View.VISIBLE
            isValid = false
        }

        if (!isValid) return

        if (newPassInput != confirmPassInput) {
            tvErrorMismatch.visibility = View.VISIBLE
            return
        }

        DialogUtils.showUniversalDialog(
            context = this,
            message = "Are you sure you want to change your password?",
            isConfirmation = true,
            onConfirm = {
                checkDatabaseAndUpdate(oldPassInput, newPassInput)
            }
        )
    }

    private fun checkDatabaseAndUpdate(oldPassInput: String, newPassInput: String) {
        val sharedPref = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val loginKey = sharedPref.getString("LOGIN_KEY", null) ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@ChangePassword)
            val dao = db.accountDao()

            // PERHATIKAN: Kita paksa pakai 'com.example.pbo.data.Account' biar tidak salah ambil
            val currentAccount: com.example.pbo.data.Account? = dao.getAccountByEmailOrPhone(loginKey)

            withContext(Dispatchers.Main) {
                if (currentAccount != null) {
                    // Sekarang '.password' pasti ketemu karena sudah pakai Account yang benar
                    if (currentAccount.password == oldPassInput) {
                        updatePasswordInDB(loginKey, newPassInput)
                    } else {
                        tvErrorWrongOld.visibility = View.VISIBLE
                    }
                } else {
                    // Handle jika akun tidak ditemukan
                }
            }
        }
    }

    private fun updatePasswordInDB(loginKey: String, newPass: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(this@ChangePassword)

            // Update password
            db.accountDao().updatePassword(loginKey, newPass)

            withContext(Dispatchers.Main) {
                DialogUtils.showUniversalDialog(
                    context = this@ChangePassword,
                    message = "Your password has been changed.",
                    isConfirmation = false,
                    onDismiss = {
                        finish()
                    }
                )
            }
        }
    }
}