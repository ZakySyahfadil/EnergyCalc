// File: repository/HistoryRepository.kt
package com.example.pbo.Nazwar.repository

import com.example.pbo.data.HistoryEntity

interface HistoryRepository {
    suspend fun saveHistory(entity: HistoryEntity)
}