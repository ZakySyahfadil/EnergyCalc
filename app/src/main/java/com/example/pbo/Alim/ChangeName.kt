package com.example.pbo.Alim

import android.content.Intent
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
import com.example.pbo.viewmodel.ChangeNameViewModel
import com.example.pbo.viewmodel.ViewModelFactory

// [SRP] Activity ini hanya bertanggung jawab MENAMPILKAN UI dan menangani Input User.
// Logika penyimpanan data sudah dipindah ke ViewModel & Repository.

class ChangeName : AppCompatActivity() {

    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var errorText: TextView
    private lateinit var viewModel: ChangeNameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_name)

        // Setup Dependency (Manual Injection)
        val repository = UserRepositoryImpl(this)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ChangeNameViewModel::class.java]

        initViews()
        observeViewModel()
    }

    private fun initViews() {
        firstName = findViewById(R.id.firstname)
        lastName = findViewById(R.id.lastname)
        errorText = findViewById(R.id.enteryourname)
        val saveButton: Button = findViewById(R.id.savebutton)

        saveButton.setOnClickListener {
            val first = firstName.text.toString().trim()
            val last = lastName.text.toString().trim()

            // UI Logic: Konfirmasi user sebelum aksi logic
            DialogUtils.showUniversalDialog(
                context = this,
                message = "Are you sure you want to change your name?",
                isConfirmation = true,
                onConfirm = {
                    // Panggil Logic di ViewModel
                    val isValid = viewModel.validateAndSaveName(first, last)
                    if (!isValid) errorText.visibility = View.VISIBLE else errorText.visibility = View.GONE
                }
            )
        }

        findViewById<View>(R.id.btn_back).setOnClickListener { finish() }
    }

    private fun observeViewModel() {
        // [Observer Pattern] Activity bereaksi terhadap perubahan state di ViewModel
        viewModel.updateStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                val returnIntent = Intent().apply {
                    putExtra("UPDATED_NAME", "${firstName.text} ${lastName.text}")
                }
                setResult(RESULT_OK, returnIntent)

                DialogUtils.showUniversalDialog(
                    context = this,
                    message = "Your name has been updated.",
                    isConfirmation = false,
                    onDismiss = { finish() }
                )
            }
        }
    }
}