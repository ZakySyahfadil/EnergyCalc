package com.example.pbo.Nazwar.repository

import com.example.pbo.Nazwar.data.UserPreferences

class UserRepositoryImpl(
    private val userPreferences: UserPreferences
) : UserRepository {

    override fun getFullName(): String {
        return userPreferences.getFullName()
    }
}