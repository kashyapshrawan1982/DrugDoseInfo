package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.DoseCalculationResult
import com.example.model.Drug
import com.example.model.Formulation
import com.example.model.FormulationType
import com.example.model.IndicationRegimen
import com.example.util.WeightEstimator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.ceil

class DoseCalculatorViewModel : ViewModel() {

    private val _calculationResult = MutableStateFlow<DoseCalculationResult?>(null)
    val calculationResult: StateFlow<DoseCalculationResult?> = _calculationResult.asStateFlow()

    fun estimateWeight(years: Int, months: Int): Double {
        return WeightEstimator.estimateWeightKg(years, months)
    }

    fun calculateDose(
        drug: Drug,
        formulation: Formulation,
        regimen: IndicationRegimen?,
        patientWeightKg: Double,
        patientAgeYears: Int,
        patientAgeMonths: Int,
        customMgPerKg: Double? = null,
        isPerDose: Boolean = false
    ) {
        val activeRegimen = regimen ?: drug.defaultRegimen
        val mgPerKg = customMgPerKg ?: if (isPerDose) {
            activeRegimen.defaultMgPerKgPerDose ?: (activeRegimen.defaultMgPerKgPerDay / activeRegimen.frequencyTimesPerDay.coerceAtLeast(1))
        } else {
            activeRegimen.defaultMgPerKgPerDay
        }

        val frequencyTimesPerDay = activeRegimen.frequencyTimesPerDay.coerceAtLeast(1)

        val rawCalculatedDailyDoseMg: Double
        val rawCalculatedSingleDoseMg: Double

        if (isPerDose) {
            rawCalculatedSingleDoseMg = mgPerKg * patientWeightKg
            rawCalculatedDailyDoseMg = rawCalculatedSingleDoseMg * frequencyTimesPerDay
        } else {
            rawCalculatedDailyDoseMg = mgPerKg * patientWeightKg
            rawCalculatedSingleDoseMg = rawCalculatedDailyDoseMg / frequencyTimesPerDay
        }

        val maxSingleDose = activeRegimen.maxSingleDoseMg
        val maxDailyDose = activeRegimen.maxDailyDoseMg

        // Variables properly declared before use:
        val isExceedingMaxSingle = maxSingleDose > 0.0 && rawCalculatedSingleDoseMg > maxSingleDose
        val isExceedingMaxDaily = maxDailyDose > 0.0 && rawCalculatedDailyDoseMg > maxDailyDose

        val cappedSingleDoseMg = if (isExceedingMaxSingle) maxSingleDose else rawCalculatedSingleDoseMg
        val cappedDailyDoseMg = if (isExceedingMaxDaily) maxDailyDose else rawCalculatedDailyDoseMg

        val singleDoseMl = if (formulation.mgPerMl > 0.0) {
            cappedSingleDoseMg / formulation.mgPerMl
        } else {
            0.0
        }

        val singleDoseTablets = if (formulation.type == FormulationType.TABLET || formulation.type == FormulationType.CAPSULE) {
            if (formulation.concentrationMg > 0.0) cappedSingleDoseMg / formulation.concentrationMg else 0.0
        } else {
            null
        }

        val totalDailyDoseMl = singleDoseMl * frequencyTimesPerDay
        val totalCourseVolumeMl = totalDailyDoseMl * activeRegimen.standardDurationDays

        val bottlesSummary = if (formulation.volumeMl > 0.0 && totalCourseVolumeMl > 0.0) {
            val count = ceil(totalCourseVolumeMl / formulation.volumeMl).toInt()
            "$count bottle(s) of ${formulation.volumeMl} ml"
        } else {
            ""
        }

        val hasSafetyThresholdAlert = isExceedingMaxSingle || isExceedingMaxDaily

        val result = DoseCalculationResult(
            drug = drug,
            formulation = formulation,
            singleDoseMg = cappedSingleDoseMg,
            singleDoseMl = singleDoseMl,
            singleDoseTablets = singleDoseTablets,
            dailyDoseMg = cappedDailyDoseMg,
            totalDailyDoseMg = cappedDailyDoseMg,
            totalDailyDoseMl = totalDailyDoseMl,
            frequencyDescription = activeRegimen.frequencyDescription,
            frequencyText = activeRegimen.frequencyDescription,
            instructions = drug.administrationAdvice,
            bottlesNeededSummary = bottlesSummary,
            courseDurationDays = activeRegimen.standardDurationDays,
            stepByStepExplanation = "Calculated at $mgPerKg mg/kg for weight $patientWeightKg kg",
            warnings = drug.warnings,
            patientWeightKg = patientWeightKg,
            isExceedingMaxSingleDose = isExceedingMaxSingle,
            isExceedingMaxDailyDose = isExceedingMaxDaily,
            isBelowMinAge = ((patientAgeYears * 12) + patientAgeMonths) < drug.minAgeMonths,
            rawCalculatedSingleDoseMg = rawCalculatedSingleDoseMg,
            maxSingleDoseMg = maxSingleDose,
            rawCalculatedDailyDoseMg = rawCalculatedDailyDoseMg,
            maxDailyDoseMg = maxDailyDose,
            regimen = activeRegimen,
            patientAgeYears = patientAgeYears,
            patientAgeMonths = patientAgeMonths,
            mgPerKgUsed = mgPerKg,
            isPerDoseCalculation = isPerDose,
            frequencyTimesPerDay = frequencyTimesPerDay,
            totalCourseVolumeMl = totalCourseVolumeMl,
            cappedSingleDoseMg = cappedSingleDoseMg,
            cappedDailyDoseMg = cappedDailyDoseMg,
            hasSafetyThresholdAlert = hasSafetyThresholdAlert
        )

        _calculationResult.value = result
    }
}