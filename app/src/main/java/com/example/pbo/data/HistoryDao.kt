package com.example.pbo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {

    // Simpan data history baru
    @Insert
    suspend fun insert(history: HistoryEntity)

    // Hapus history berdasarkan ID
    @Query("DELETE FROM history_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    // 🔥 UPDATED: Ambil semua history KHUSUS milik user tertentu ('owner')
    // Diurutkan dari yang paling baru (DESC)
    @Query("SELECT * FROM history_table WHERE userOwner = :owner ORDER BY id DESC")
    suspend fun getAllHistory(owner: String): List<HistoryEntity>

    // Ambil satu detail history (misal untuk halaman detail)
    @Query("SELECT * FROM history_table WHERE id = :id LIMIT 1")
    fun getHistoryById(id: Int): HistoryEntity?
}