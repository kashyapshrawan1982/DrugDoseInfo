package com.example.model

enum class DrugCategory {
    ALL, 
    ANTIBIOTIC, 
    ANTIPYRETIC_ANALGESIC, 
    OTHER;

    val displayName: String 
        get() = name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
}

enum class FormulationType {
    ALL, 
    SYRUP, 
    ORAL_SUSPENSION, 
    DROPS, 
    TABLET, 
    CAPSULE, 
    OTHER;

    val displayName: String 
        get() = name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
}

data class Formulation(
    val id: String,
    val name: String,
    val type: FormulationType,
    val concentrationMg: Double,
    val volumeMl: Double,
    val unitLabel: String = "",
    val standardDoses: List<Double> = emptyList(),
    val bottleSizesMl: List<Double> = emptyList()
) {
    val displayName: String get() = name
    val mgPerMl: Double get() = if (volumeMl > 0.0) concentrationMg / volumeMl else 0.0
}

data class IndicationRegimen(
    val id: String,
    val name: String,
    val description: String,
    val defaultMgPerKgPerDay: Double = 0.0,
    val defaultMgPerKgPerDose: Double? = null,
    val frequencyTimesPerDay: Int = 1,
    val frequencyDescription: String = "",
    val maxDailyDoseMg: Double = 0.0,
    val maxSingleDoseMg: Double = 0.0,
    val minAgeMonths: Int = 0,
    val standardDurationDays: Int = 1
) {
    val displayName: String get() = name
}

data class IndianBrand(
    val name: String,
    val company: String,
    val forms: String,
    val packaging: String
) {
    val displayName: String get() = name
}

data class Drug(
    val id: String,
    val name: String,
    val genericName: String,
    val category: DrugCategory,
    val subtitle: String = "",
    val description: String = "",
    val standardRegimenSummary: String = "",
    val adultDoseSummary: String = "",
    val minAgeMonths: Int = 0,
    val defaultRegimen: IndicationRegimen,
    val alternativeRegimens: List<IndicationRegimen> = emptyList(),
    val formulations: List<Formulation> = emptyList(),
    val indications: List<String> = emptyList(),
    val contraindications: List<String> = emptyList(),
    val warnings: List<String> = emptyList(),
    val sideEffects: List<String> = emptyList(),
    val administrationAdvice: String = "",
    val reconstitutionStorage: String = "",
    val renalAdjustmentNote: String = "",
    val references: List<String> = emptyList(),
    val indianBrands: List<IndianBrand> = emptyList(),
    val isCustom: Boolean = false,
    val lastEditedTimestamp: Long = 0L
) {
    val displayName: String get() = name
}

data class DoseCalculationResult(
    val drug: Drug,
    val formulation: Formulation,
    val singleDoseMg: Double = 0.0,
    val singleDoseMl: Double = 0.0,
    val singleDoseTablets: Double? = null,
    val dailyDoseMg: Double = 0.0,
    val totalDailyDoseMg: Double = 0.0,
    val totalDailyDoseMl: Double = 0.0,
    val frequencyDescription: String = "",
    val frequencyText: String = "",
    val instructions: String = "",
    val bottlesNeededSummary: String = "",
    val courseDurationDays: Int = 0,
    val stepByStepExplanation: String = "",
    val warnings: List<String> = emptyList(),
    val patientWeightKg: Double = 0.0,
    
    // 👇 Yeh 3 nayi lines add ki hain PdfReportGenerator ke liye 👇
    val patientAgeYears: Int = 0,
    val patientAgeMonths: Int = 0,
    val hasSafetyThresholdAlert: Boolean = false,
    
    val isExceedingMaxSingleDose: Boolean = false,
    val isExceedingMaxDailyDose: Boolean = false,
    val isBelowMinAge: Boolean = false,
    val rawCalculatedSingleDoseMg: Double = 0.0,
    val maxSingleDoseMg: Double = 0.0,
    val rawCalculatedDailyDoseMg: Double = 0.0,
    val maxDailyDoseMg: Double = 0.0,
    val regimen: IndicationRegimen? = null
)
