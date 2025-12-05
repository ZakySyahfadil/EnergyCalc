package com.example.pbo.Nazwar.calculation

interface UsageCalculator {
    fun calculate(durationText: String?, frequencyText: String?): Pair<Int, Int>
}
