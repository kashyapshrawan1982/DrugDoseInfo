package com.example.model

enum class FormulationType {
  ORAL_SUSPENSION,
  SYRUP,
  DROPS,
  TABLET,
  CAPSULE,
  INJECTION
}

enum class DrugCategory(val displayName: String) {
  ALL("All"),
  ANTIBIOTIC("Antibiotics"),
  ANTIPYRETIC_ANALGESIC("Antipyretics & Pain"),
  RESPIRATORY("Respiratory & Asthma"),
  ANTIHISTAMINE("Antihistamines"),
  GASTROINTESTINAL("Gastrointestinal"),
  CORTICOSTEROID("Steroids & Allergy"),
  ANTICONVULSANT("Anticonvulsants"),
  CARDIOVASCULAR("Cardiovascular"),
  OTHER("Other / Custom")
}

data class Formulation(
  val id: String,
  val name: String,
  val type: FormulationType,
  val concentrationMg: Double, // e.g. 125 mg
  val volumeMl: Double = 5.0,  // e.g. 5 mL (1.0 for drops or tablets)
  val unitLabel: String = "mg / 5mL", // Display label
  val bottleSizesMl: List<Double> = listOf(60.0, 100.0) // Common bottle sizes
) {
  // Returns mg per 1 mL
  val mgPerMl: Double
    get() = if (volumeMl > 0) concentrationMg / volumeMl else concentrationMg
}

data class IndicationRegimen(
  val id: String,
  val name: String,
  val description: String,
  val defaultMgPerKgPerDay: Double,
  val defaultMgPerKgPerDose: Double? = null,
  val frequencyTimesPerDay: Int, // 1 = q24h, 2 = q12h, 3 = q8h, 4 = q6h
  val frequencyDescription: String,
  val maxDailyDoseMg: Double,
  val maxSingleDoseMg: Double,
  val minAgeMonths: Int = 0,
  val standardDurationDays: Int = 7
)

data class IndianBrand(
  val name: String,
  val manufacturer: String,
  val forms: String,
  val packaging: String = "60 mL / 100 mL"
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
  val minAgeMonths: Int = 0,
  val defaultRegimen: IndicationRegimen,
  val alternativeRegimens: List<IndicationRegimen> = emptyList(),
  val formulations: List<Formulation>,
  val indications: List<String>,
  val contraindications: List<String>,
  val warnings: List<String>,
  val sideEffects: List<String>,
  val administrationAdvice: String,
  val reconstitutionStorage: String? = null,
  val renalAdjustmentNote: String? = null,
  val references: List<String> = emptyList(),
  val indianBrands: List<IndianBrand> = emptyList(),
  val isCustom: Boolean = false,
  val lastEditedTimestamp: Long? = null
)

data class DoseCalculationResult(
  val drug: Drug,
  val patientAgeYears: Float,
  val patientAgeMonths: Int,
  val patientWeightKg: Double,
  val formulation: Formulation,
  val regimen: IndicationRegimen,
  val mgPerKgUsed: Double,
  val isPerDoseCalculation: Boolean,
  
  // Calculated values
  val singleDoseMg: Double,
  val singleDoseMl: Double?,
  val singleDoseTablets: Double?,
  val totalDailyDoseMg: Double,
  val totalDailyDoseMl: Double?,
  
  // Regimen metadata
  val frequencyText: String,
  val frequencyTimesPerDay: Int,
  val courseDurationDays: Int,
  val totalCourseVolumeMl: Double?,
  val bottlesNeededSummary: String?,
  
  // Clinical validation alerts & Threshold details
  val isExceedingMaxSingleDose: Boolean,
  val isExceedingMaxDailyDose: Boolean,
  val isBelowMinAge: Boolean,
  val rawCalculatedSingleDoseMg: Double = singleDoseMg,
  val rawCalculatedDailyDoseMg: Double = totalDailyDoseMg,
  val maxSingleDoseMg: Double = regimen.maxSingleDoseMg,
  val maxDailyDoseMg: Double = regimen.maxDailyDoseMg,
  val cappedSingleDoseMg: Double,
  val cappedDailyDoseMg: Double,
  val warnings: List<String>,
  val stepByStepExplanation: String
) {
  val hasSafetyThresholdAlert: Boolean
    get() = isExceedingMaxSingleDose || isExceedingMaxDailyDose || isBelowMinAge
}

data class CalculationHistoryItem(
  val id: String,
  val timestamp: Long,
  val patientNameOrId: String,
  val patientAgeText: String,
  val patientWeightKg: Double,
  val drugName: String,
  val formulationText: String,
  val singleDoseText: String,
  val frequencyText: String,
  val totalDailyText: String,
  val durationDays: Int,
  val courseTotalText: String
)
