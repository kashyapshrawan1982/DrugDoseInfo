package com.example.model

data class CalculationHistoryItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val drugName: String = "",
    val patientWeight: Double = 0.0,
    val calculatedDose: String = "",
    val durationDays: Int = 0,           // <-- Yeh naya add kiya
    val courseTotalText: String = "",    // <-- Yeh naya add kiya
    val timestamp: Long = System.currentTimeMillis()
)