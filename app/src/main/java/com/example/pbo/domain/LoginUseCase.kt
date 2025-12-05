package com.example.pbo.domain

import com.example.pbo.data.repository.AccountRepository
import com.example.pbo.data.Account

class LoginUseCase(private val repo: AccountRepository) {

    suspend fun execute(input: String, pass: String): LoginResult {

        if (input.isBlank()) return LoginResult.InputEmpty
        if (pass.isBlank()) return LoginResult.PasswordEmpty

        val account = repo.getAccountByInput(input)
            ?: return LoginResult.NotRegistered

        if (account.password != pass)
            return LoginResult.WrongPassword

        return LoginResult.Success(account)
    }
}

sealed class LoginResult {
    object InputEmpty : LoginResult()
    object PasswordEmpty : LoginResult()
    object NotRegistered : LoginResult()
    object WrongPassword : LoginResult()
    data class Success(val account: Account) : LoginResult()
}