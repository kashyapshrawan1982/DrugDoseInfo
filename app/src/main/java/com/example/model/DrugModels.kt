package com.example.model

enum class DrugCategory {
    ANTIBIOTIC, ANTIPYRETIC_ANALGESIC, OTHER
}

enum class FormulationType {
    SYRUP, ORAL_SUSPENSION, DROPS, TABLET, OTHER
}

data class Formulation(
    val id: String,
    val name: String,
    val type: FormulationType,
    val concentrationMg: Double,
    val volumeMl: Double,
    val unitLabel: String,
    val standardDoses: List<Double> = emptyList()
)

data class IndicationRegimen(
    val id: String,
    val name: String,
    val description: String,
    val defaultMgPerKgPerDay: Double = 0.0,
    val defaultMgPerKgPerDose: Double? = null,
    val frequencyTimesPerDay: Int,
    val frequencyDescription: String,
    val maxDailyDoseMg: Double,
    val maxSingleDoseMg: Double,
    val minAgeMonths: Int = 0,
    val standardDurationDays: Int
)

data class IndianBrand(
    val name: String,
    val company: String,
    val forms: String,
    val packaging: String
)

data class Drug(
    val id: String,
    val name: String,
    val genericName: String,
    val category: DrugCategory,
    val subtitle: String,
    val description: String,
    val standardRegimenSummary: String,
    val adultDoseSummary: String,
    val minAgeMonths: Int,
    val defaultRegimen: IndicationRegimen,
    val alternativeRegimens: List<IndicationRegimen> = emptyList(),
    val formulations: List<Formulation>,
    val indications: List<String>,
    val contraindications: List<String>,
    val warnings: List<String>,
    val sideEffects: List<String>,
    val administrationAdvice: String,
    val reconstitutionStorage: String,
    val renalAdjustmentNote: String = "",
    val references: List<String> = emptyList(),
    val indianBrands: List<IndianBrand> = emptyList(),
    val isCustom: Boolean = false
)

// Yahi wo class hai jiska error aa raha tha!
data class DoseCalculationResult(
    val drug: Drug,
    val formulation: Formulation,
    val singleDoseMg: Double,
    val singleDoseMl: Double,
    val dailyDoseMg: Double,
    val frequencyDescription: String,
    val instructions: String = "",
    val bottlesNeededSummary: String = "", 
    val courseDurationDays: Int = 0
)