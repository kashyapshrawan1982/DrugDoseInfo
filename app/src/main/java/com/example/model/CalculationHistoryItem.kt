package com.example.model

data class CalculationHistoryItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val drugName: String = "",
    val formulationText: String = "",
    val patientNameOrId: String = "",
    val patientAgeText: String = "",
    val patientAgeYears: Int = 0,
    val patientAgeMonths: Int = 0,
    val patientWeightKg: Double = 0.0,
    val calculatedDose: String = "",
    val singleDoseText: String = "",
    val frequencyText: String = "",
    val totalDailyText: String = "",
    val durationDays: Int = 0,
    val courseTotalText: String = "",
    val timestamp: Long = System.currentTimeMillis()
)