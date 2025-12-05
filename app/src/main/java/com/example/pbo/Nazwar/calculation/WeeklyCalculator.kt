package com.example.pbo.Nazwar.calculation

class WeeklyCalculator : UsageCalculator {
    override fun calculate(durationText: String?, frequencyText: String?): Pair<Int, Int> {
        return Pair(durationText!!.toInt(), frequencyText!!.toInt())
    }
}