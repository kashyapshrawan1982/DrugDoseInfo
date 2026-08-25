package com.example.model

data class CalculationHistoryItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val drugName: String,
    val patientWeight: Double,
    val calculatedDose: String,
    val timestamp: Long = System.currentTimeMillis()
)