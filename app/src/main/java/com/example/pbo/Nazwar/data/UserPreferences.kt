package com.example.pbo.Nazwar.data

import android.content.Context

class UserPreferences(context: Context) {

    companion object {
        private const val PREFS_NAME = "USER_PREFS"
        private const val KEY_FIRSTNAME = "firstname"
        private const val KEY_LASTNAME = "lastname"
        private const val KEY_LOGIN = "LOGIN_KEY"
    }

    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // --- Nama User ---
    fun getFirstName(): String = prefs.getString(KEY_FIRSTNAME, "User") ?: "User"
    fun getLastName(): String = prefs.getString(KEY_LASTNAME, "") ?: ""

    fun getFullName(): String = buildString {
        append(getFirstName())
        if (getLastName().isNotBlank()) append(" ").append(getLastName())
    }.trim()

    fun saveName(firstname: String, lastname: String) {
        prefs.edit()
            .putString(KEY_FIRSTNAME, firstname)
            .putString(KEY_LASTNAME, lastname)
            .apply()
    }

    // --- Login State (dipakai di SaveCalculationResultUseCase) ---
    fun getCurrentUser(): String? = prefs.getString(KEY_LOGIN, null)

    fun saveLoginKey(emailOrUsername: String) {
        prefs.edit().putString(KEY_LOGIN, emailOrUsername).apply()
    }

    fun clearLogin() {
        prefs.edit().remove(KEY_LOGIN).apply()
    }
}