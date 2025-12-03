package com.example.pbo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val deviceName: String,
    val kWh: String,
    val totalCost: String,
    val date: String
)