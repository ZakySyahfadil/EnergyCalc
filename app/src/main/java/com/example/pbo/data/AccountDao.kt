package com.example.pbo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AccountDao {
    @Insert
    suspend fun insertAccount(account: Account)

    @Query("SELECT * FROM accounts WHERE email = :email LIMIT 1")
    suspend fun getAccountByEmail(email: String): Account?

    @Query("SELECT * FROM accounts WHERE phoneNumber = :phone LIMIT 1")
    suspend fun getAccountByPhone(phone: String): Account?

    @Query("SELECT * FROM accounts WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): Account?

    @Query("UPDATE accounts SET password = :newPassword WHERE email = :email")
    suspend fun updatePasswordByEmail(email: String, newPassword: String)

    @Query("SELECT * FROM accounts WHERE email = :input OR phoneNumber = :input LIMIT 1")
    suspend fun getAccountByEmailOrPhone(input: String): Account?

}

