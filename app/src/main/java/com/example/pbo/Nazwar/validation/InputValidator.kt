package com.example.pbo.Nazwar.validation

data class ValidationResult(
    val isValid: Boolean,
    val nameError: Boolean = false,
    val powerError: Boolean = false,
    val durationError: Boolean = false,
    val frequencyError: Boolean = false,
    val bothDurationFrequencyError: Boolean = false
)

class InputValidator {

    fun validate(
        name: String,
        power: String,
        duration: String?,
        frequency: String?,
        isAlwaysOn: Boolean
    ): ValidationResult {

        val nameError = name.isBlank()
        val powerError = power.isBlank()

        var durationError = false
        var frequencyError = false
        var bothError = false

        if (!isAlwaysOn) {
            val durEmpty = duration.isNullOrBlank()
            val freqEmpty = frequency.isNullOrBlank()

            if (durEmpty && freqEmpty) {
                bothError = true
            } else {
                if (durEmpty) durationError = true
                if (freqEmpty) frequencyError = true
            }
        }
        return ValidationResult(
            isValid = !nameError && !powerError && !durationError && !frequencyError && !bothError,
            nameError = nameError,
            powerError = powerError,
            durationError = durationError,
            frequencyError = frequencyError,
            bothDurationFrequencyError = bothError
        )
    }
}