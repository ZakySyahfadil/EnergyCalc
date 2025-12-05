package com.example.pbo.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.pbo.data.Account
import com.example.pbo.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// [DIP - Dependency Inversion Principle]
// Modul tingkat tinggi (ViewModel) tidak boleh bergantung pada modul tingkat rendah (Database langsung).
// Keduanya bergantung pada Abstraksi (Interface ini).
interface UserRepository {
    suspend fun updateName(loginKey: String, first: String, last: String)
    suspend fun updatePassword(loginKey: String, newPass: String)
    suspend fun getAccount(loginKey: String): Account?
    fun saveUserToPrefs(first: String, last: String)
    fun getUserFromPrefs(): Pair<String, String>
    fun getLoginKey(): String?
    fun logout()
}

// [SRP - Single Responsibility Principle]
// Kelas ini HANYA bertanggung jawab mengurus DATA (Database & SharedPreferences).
// Tidak ada urusan UI di sini.
class UserRepositoryImpl(private val context: Context) : UserRepository {

    private val db = AppDatabase.getDatabase(context)
    private val prefs = context.getSharedPreferences("USER_PREFS", MODE_PRIVATE)

    override suspend fun updateName(loginKey: String, first: String, last: String) {
        withContext(Dispatchers.IO) {
            db.accountDao().updateName(loginKey, first, last)
        }
    }

    override suspend fun updatePassword(loginKey: String, newPass: String) {
        withContext(Dispatchers.IO) {
            db.accountDao().updatePassword(loginKey, newPass)
        }
    }

    override suspend fun getAccount(loginKey: String): Account? {
        return withContext(Dispatchers.IO) {
            db.accountDao().getAccountByEmailOrPhone(loginKey)
        }
    }

    override fun saveUserToPrefs(first: String, last: String) {
        prefs.edit().putString("firstname", first).putString("lastname", last).apply()
    }

    override fun getUserFromPrefs(): Pair<String, String> {
        val first = prefs.getString("firstname", "") ?: ""
        val last = prefs.getString("lastname", "") ?: ""
        return Pair(first, last)
    }

    override fun getLoginKey(): String? {
        return prefs.getString("LOGIN_KEY", null)
    }

    override fun logout() {
        prefs.edit().clear().apply()
    }
}