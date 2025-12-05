package com.example.pbo.Nazwar.repository

import com.example.pbo.data.HistoryDao
import com.example.pbo.data.HistoryEntity

class HistoryRepositoryImpl(
    private val dao: HistoryDao
) : HistoryRepository {
    override suspend fun saveHistory(entity: HistoryEntity) {
        dao.insert(entity)
    }
}