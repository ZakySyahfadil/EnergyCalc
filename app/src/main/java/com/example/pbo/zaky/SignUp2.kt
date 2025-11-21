package com.example.pbo.zaky

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pbo.R
import com.example.pbo.zaky.SignUp3

class SignUp2 : AppCompatActivity() {

    private lateinit var otpBoxes: List<EditText>
    private lateinit var tvExpire: TextView
    private lateinit var tvExpiredError: TextView
    private lateinit var tvCodeError: TextView
    private lateinit var tvCodeFail: TextView
    private lateinit var btnContinue: Button
    private lateinit var btnGetNewCode: TextView

    private var currentOtp = "111111"     // OTP default
    private var isExpired = false
    private var timer: CountDownTimer? = null

    // =====================================
    //   AMBIL EMAIL DARI SIGNUP 1 DI SINI
    // =====================================
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)

        // Ambil email yang dikirim dari SignUp1
        email = intent.getStringExtra("email")

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        otpBoxes = listOf(
            findViewById(R.id.otp1),
            findViewById(R.id.otp2),
            findViewById(R.id.otp3),
            findViewById(R.id.otp4),
            findViewById(R.id.otp5),
            findViewById(R.id.otp6)
        )

        tvExpire = findViewById(R.id.limamenit)
        tvExpiredError = findViewById(R.id.expired)
        tvCodeError = findViewById(R.id.tvCodeError)
        tvCodeFail = findViewById(R.id.tvCodeFail)
        btnContinue = findViewById(R.id.Continue)
        btnGetNewCode = findViewById(R.id.getnewcode)

        startExpireTimer()
        setupOtpAutoMove()

        btnContinue.setOnClickListener { validateOtp() }
        btnGetNewCode.setOnClickListener { regenerateOtp() }
    }


    // ---------------- TIMER --------------------
    private fun startExpireTimer() {
        isExpired = false
        tvExpiredError.visibility = TextView.GONE
        tvExpire.visibility = TextView.VISIBLE

        timer?.cancel()
        timer = object : CountDownTimer(5 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                isExpired = true
                tvExpire.visibility = TextView.GONE
                tvExpiredError.visibility = TextView.VISIBLE
            }
        }.start()
    }


    // -------------- GET NEW CODE ---------------
    private fun regenerateOtp() {
        currentOtp = "000000"   // OTP baru
        clearOtpInput()
        hideAllErrors()
        startExpireTimer()
    }


    // ---------------- VALIDATE OTP -------------
    private fun validateOtp() {
        val userOtp = otpBoxes.joinToString("") { it.text.toString() }

        hideAllErrors()

        // Kode expired
        if (isExpired) {
            tvExpiredError.visibility = TextView.VISIBLE
            return
        }

        // OTP kosong
        if (userOtp.isEmpty()) {
            tvCodeError.visibility = TextView.VISIBLE
            return
        }

        // OTP salah
        if (userOtp != currentOtp) {
            tvCodeFail.visibility = TextView.VISIBLE
            clearOtpInput()
            return
        }

        // ================================
        //   EMAIL WAJIB ADA DI SINI
        // ================================
        if (email.isNullOrEmpty()) {
            Toast.makeText(this, "Email hilang. Silakan ulangi proses Sign Up.", Toast.LENGTH_LONG).show()
            return
        }

        // OTP benar → lanjut ke SignUp3 sambil membawa email
        val intent = Intent(this, SignUp3::class.java).apply {
            putExtra("email", email)
        }
        startActivity(intent)
    }


    private fun hideAllErrors() {
        tvCodeError.visibility = TextView.GONE
        tvCodeFail.visibility = TextView.GONE
        tvExpiredError.visibility = TextView.GONE
    }


    private fun clearOtpInput() {
        otpBoxes.forEach { it.setText("") }
        otpBoxes[0].requestFocus()
    }


    // ------------ AUTO MOVE KOTAK OTP ------------
    private fun setupOtpAutoMove() {
        for (i in otpBoxes.indices) {
            otpBoxes[i].addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < otpBoxes.size - 1) {
                        otpBoxes[i + 1].requestFocus()
                    } else if (s?.isEmpty() == true && i > 0) {
                        otpBoxes[i - 1].requestFocus()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    tvCodeError.visibility = TextView.GONE
                }
            })
        }
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}