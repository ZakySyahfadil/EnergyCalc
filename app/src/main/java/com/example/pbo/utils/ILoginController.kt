package com.example.pbo.utils

interface ILoginController {
    fun validateLogin(email: String, password: String): Boolean
}