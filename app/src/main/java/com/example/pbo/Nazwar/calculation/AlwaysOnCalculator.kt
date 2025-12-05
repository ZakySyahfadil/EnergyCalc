package com.example.pbo.Nazwar.calculation

class AlwaysOnCalculator : UsageCalculator {
    override fun calculate(durationText: String?, frequencyText: String?): Pair<Int, Int> {
        return Pair(168 *60,1)
    }
}