package com.example.pbo.data.repository

import com.example.pbo.data.Account
import com.example.pbo.data.AccountDao

class AccountRepository(private val dao: AccountDao) {

    suspend fun getAccountByInput(input: String): Account? {
        return if (input.contains("@")) {
            dao.getAccountByEmail(input.lowercase())
        } else if (input.all { it.isDigit() }) {
            dao.getAccountByPhone(input)
        } else {
            dao.getAccountByEmail(input.lowercase())
        }
    }
}