package com.example.pbo.Alim

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pbo.R
import com.example.pbo.data.repository.UserRepositoryImpl
import com.example.pbo.utils.DialogUtils
import com.example.pbo.viewmodel.ChangePasswordViewModel
import com.example.pbo.viewmodel.ViewModelFactory

class ChangePassword : AppCompatActivity() {

    private lateinit var etOldPass: EditText
    private lateinit var etNewPass: EditText
    private lateinit var etConfirmPass: EditText
    private lateinit var tvErrorWrongOld: TextView
    private lateinit var tvErrorMismatch: TextView
    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_password)

        val repository = UserRepositoryImpl(this)
        viewModel = ViewModelProvider(this, ViewModelFactory(repository))[ChangePasswordViewModel::class.java]

        initView()
        observeViewModel()
    }

    private fun initView() {
        etOldPass = findViewById(R.id.enteryouroldpass)
        etNewPass = findViewById(R.id.enteryournewpass)
        etConfirmPass = findViewById(R.id.confirmyournewpass)
        tvErrorWrongOld = findViewById(R.id.wrong4)
        tvErrorMismatch = findViewById(R.id.wrong5)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            // UI Logic: Hapus error lama sebelum proses baru
            hideErrors()

            DialogUtils.showUniversalDialog(
                context = this,
                message = "Are you sure you want to change your password?",
                isConfirmation = true,
                onConfirm = {
                    // Panggil ViewModel untuk memproses logika
                    viewModel.changePassword(
                        etOldPass.text.toString().trim(),
                        etNewPass.text.toString().trim(),
                        etConfirmPass.text.toString().trim()
                    )
                }
            )
        }
        findViewById<View>(R.id.btn_back).setOnClickListener { finish() }
    }

    private fun hideErrors() {
        tvErrorWrongOld.visibility = View.GONE
        tvErrorMismatch.visibility = View.GONE
    }

    private fun observeViewModel() {
        viewModel.passwordStatus.observe(this) { status ->
            when (status) {
                "SUCCESS" -> {
                    DialogUtils.showUniversalDialog(
                        context = this,
                        message = "Your password has been changed.",
                        isConfirmation = false,
                        onDismiss = { finish() }
                    )
                }
                "WRONG_OLD" -> tvErrorWrongOld.visibility = View.VISIBLE
                "MISMATCH" -> tvErrorMismatch.visibility = View.VISIBLE
            }
        }
    }
}