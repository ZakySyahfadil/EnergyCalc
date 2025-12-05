// File: formatter/CalculationFormatter.kt
package com.example.pbo.Nazwar.formatter

object CalculationFormatter {

    fun formatEnergyFormula(
        powerWatt: Int,
        durationHour: Double,  // sudah dalam jam
        frequencyWeek: Int
    ): String {
        return "= ($powerWatt × ${"%.2f".format(durationHour * frequencyWeek)}) ÷ 1000"
    }

    fun formatWeeklyEnergy(energyKwh: Double): String {
        return "= ${"%.3f".format(energyKwh)} kWh"
    }

    fun formatMonthlyCostFormula(energyPerWeekKwh: Double, tariff: Double = 1500.0): String {
        return "= ${"%.3f".format(energyPerWeekKwh)} × ${"%,.0f".format(tariff)} × 4"
    }

    fun formatMonthlyCost(cost: Double): String {
        return "Rp${"%,.0f".format(cost)}/month"
    }
}