package com.example.pbo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDao {

    // 1. Insert Akun Baru
    @Insert
    suspend fun insertAccount(account: Account)

    // 2. Cek Email (Untuk Sign Up)
    @Query("SELECT * FROM accounts WHERE email = :email LIMIT 1")
    suspend fun getAccountByEmail(email: String): Account?

    // 3. Cek No HP (Untuk Sign Up)
    @Query("SELECT * FROM accounts WHERE phoneNumber = :phone LIMIT 1")
    suspend fun getAccountByPhone(phone: String): Account?

    // 4. Login Spesifik (Email + Password)
    @Query("SELECT * FROM accounts WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): Account?

    // 5. Update Password via Email (Mungkin dipakai di Forgot Password)
    @Query("UPDATE accounts SET password = :newPassword WHERE email = :email")
    suspend fun updatePasswordByEmail(email: String, newPassword: String)

    // 6. Ambil Akun via Email ATAU No HP (Dipakai untuk Login & Change Password)
    @Query("SELECT * FROM accounts WHERE email = :input OR phoneNumber = :input LIMIT 1")
    suspend fun getAccountByEmailOrPhone(input: String): Account?

    // 7. Update Nama
    @Query("UPDATE accounts SET firstName = :first, lastName = :last WHERE email = :login OR phoneNumber = :login")
    suspend fun updateName(login: String, first: String, last: String)

    // 8. Update Password (Bisa via Email ATAU No HP) -> Dipakai di ChangePassword
    @Query("UPDATE accounts SET password = :newPassword WHERE email = :login OR phoneNumber = :login")
    suspend fun updatePassword(login: String, newPassword: String)
}