// File: usecase/SaveCalculationResultUseCase.kt
package com.example.pbo.Nazwar.usecase

import com.example.pbo.Nazwar.data.UserPreferences
import com.example.pbo.Nazwar.repository.HistoryRepository
import com.example.pbo.data.HistoryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SaveCalculationResultUseCase(
    private val historyRepository: HistoryRepository,
    private val userPreferences: UserPreferences
) {
    suspend operator fun invoke(
        deviceName: String,
        powerWatt: Int,
        durationMin: Int,
        frequencyWeek: Int,
        energyPerMonth: Double,
        costPerMonth: Double
    ): Boolean {
        val user = userPreferences.getCurrentUser() ?: return false

        val date = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        val entity = HistoryEntity(
            deviceName = deviceName,
            kWh = "%.3f kWh".format(energyPerMonth),
            totalCost = "Rp${"%,.0f".format(costPerMonth)}",
            date = date,
            powerValue = "$powerWatt W",
            durationValue = "$durationMin minutes",
            frequencyValue = "$frequencyWeek times",
            userOwner = user
        )

        historyRepository.saveHistory(entity)
        return true
    }
}