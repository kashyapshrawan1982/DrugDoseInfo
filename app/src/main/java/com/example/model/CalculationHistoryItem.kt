package com.example.model

data class CalculationHistoryItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val drugName: String = "",
    val patientWeight: Double = 0.0,         // <-- Default value 0.0 add kar di
    val calculatedDose: String = "",         // <-- Default value empty add kar di
    val timestamp: Long = System.currentTimeMillis()
)