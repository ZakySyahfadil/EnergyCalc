package com.example.pbo.utils

class LoginController : ILoginController {
    override fun validateLogin(email: String, password: String): Boolean {
        // Logic validasi sederhana — bisa kamu ganti nanti
        return email.isNotEmpty() && password.isNotEmpty()
    }
}