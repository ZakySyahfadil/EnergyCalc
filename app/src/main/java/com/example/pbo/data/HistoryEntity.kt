package com.example.pbo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val deviceName: String,
    val kWh: String,
    val totalCost: String,
    val date: String,

    // Kolom baru
    val powerValue: String,      // contoh: "500 W"
    val durationValue: String,   // contoh: "30 minutes"
    val frequencyValue: String   // contoh: "3 times"
)
