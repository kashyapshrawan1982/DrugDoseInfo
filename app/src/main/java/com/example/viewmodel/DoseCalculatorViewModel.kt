package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DrugDatabase
import com.example.model.CalculationHistoryItem
import com.example.model.DoseCalculationResult
import com.example.model.Drug
import com.example.model.DrugCategory
import com.example.model.Formulation
import com.example.model.FormulationType
import com.example.model.IndicationRegimen
import com.example.ui.theme.AppColorTheme
import com.example.ui.theme.DarkModePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import kotlin.math.ceil

enum class AppTab {
  CALCULATOR,
  DRUGS,
  DRUG_DETAILS,
  HISTORY,
  TOOLS,
  ADMIN
}

data class DoseCalculatorUiState(
  val drugList: List<Drug> = DrugDatabase.drugs,
  val selectedDrug: Drug = DrugDatabase.drugs.first(),
  val selectedRegimen: IndicationRegimen = DrugDatabase.drugs.first().defaultRegimen,
  val selectedFormulation: Formulation = DrugDatabase.drugs.first().formulations.first(),
  
  // Patient Inputs
  val patientAgeYears: Float = 5.0f,
  val patientAgeMonths: Int = 0,
  val patientWeightInput: String = "18.5",
  val isWeightInLbs: Boolean = false,
  val patientNameInput: String = "",
  
  // Custom Dose override
  val isCustomDoseActive: Boolean = false,
  val customMgPerKgInput: String = "",
  val courseDurationDays: Int = 7,
  
  // Calculated Result
  val calculationResult: DoseCalculationResult? = null,
  
  // Catalog & Navigation
  val searchQuery: String = "",
  val selectedCategory: DrugCategory = DrugCategory.ALL,
  val currentTab: AppTab = AppTab.CALCULATOR,
  val detailDrug: Drug = DrugDatabase.drugs.first(),
  
  // Admin & Drug Editing
  val isAdminAuthenticated: Boolean = false,
  val adminPin: String = com.example.util.AdminCredentialsManager.getPin(),
  val adminPassword: String = com.example.util.AdminCredentialsManager.getPassword(),
  val adminLockMode: com.example.util.AdminLockMode = com.example.util.AdminCredentialsManager.getLockMode(),
  val isAddingOrEditingDrug: Boolean = false,
  val drugBeingEdited: Drug? = null,
  
  // Theme & Appearance Customization
  val activeColorTheme: AppColorTheme = AppColorTheme.VIBRANT_PURPLE,
  val darkModePreference: DarkModePreference = DarkModePreference.SYSTEM,
  
  // Calculation History
  val history: List<CalculationHistoryItem> = emptyList(),
  
  // Quick Feedback Toast / Message
  val snackbarMessage: String? = null
)

class DoseCalculatorViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(DoseCalculatorUiState())
  val uiState: StateFlow<DoseCalculatorUiState> = _uiState.asStateFlow()

  init {
    // Initial calculation on launch
    recalculateDose()
  }

  fun setTab(tab: AppTab) {
    _uiState.update { it.copy(currentTab = tab) }
  }

  // --- ADMIN SECURITY & AUTHENTICATION ---

  fun authenticateAdmin(credential: String): Boolean {
    val currentPin = _uiState.value.adminPin
    val currentPassword = _uiState.value.adminPassword
    return if (credential == currentPin || credential == currentPassword) {
      _uiState.update {
        it.copy(
          isAdminAuthenticated = true,
          snackbarMessage = "Admin access granted"
        )
      }
      true
    } else {
      _uiState.update {
        it.copy(
          snackbarMessage = "Incorrect PIN or Password. Please try again."
        )
      }
      false
    }
  }

  fun logoutAdmin() {
    _uiState.update {
      it.copy(
        isAdminAuthenticated = false,
        snackbarMessage = "Admin session locked"
      )
    }
  }

  fun changeAdminPin(currentCredential: String, newPin: String): Boolean {
    val state = _uiState.value
    if (currentCredential != state.adminPin && currentCredential != state.adminPassword) {
      _uiState.update { it.copy(snackbarMessage = "Current PIN or Password is incorrect") }
      return false
    }
    if (newPin.length < 4 || newPin.length > 8 || !newPin.all { it.isDigit() }) {
      _uiState.update { it.copy(snackbarMessage = "New PIN must be 4 to 8 digits (numeric)") }
      return false
    }
    com.example.util.AdminCredentialsManager.savePin(newPin)
    _uiState.update {
      it.copy(
        adminPin = newPin,
        snackbarMessage = "Admin PIN successfully updated"
      )
    }
    return true
  }

  fun changeAdminPassword(currentCredential: String, newPassword: String): Boolean {
    val state = _uiState.value
    if (currentCredential != state.adminPassword && currentCredential != state.adminPin) {
      _uiState.update { it.copy(snackbarMessage = "Current Password or PIN is incorrect") }
      return false
    }
    if (newPassword.length < 4) {
      _uiState.update { it.copy(snackbarMessage = "New password must be at least 4 characters") }
      return false
    }
    com.example.util.AdminCredentialsManager.savePassword(newPassword)
    _uiState.update {
      it.copy(
        adminPassword = newPassword,
        snackbarMessage = "Admin Password successfully updated"
      )
    }
    return true
  }

  fun setAdminLockMode(mode: com.example.util.AdminLockMode) {
    com.example.util.AdminCredentialsManager.saveLockMode(mode)
    _uiState.update {
      it.copy(
        adminLockMode = mode,
        snackbarMessage = "Default lock screen set to ${if (mode == com.example.util.AdminLockMode.PIN) "Numeric PIN" else "Alphanumeric Password"}"
      )
    }
  }

  fun resetAdminCredentialsToDefaults() {
    com.example.util.AdminCredentialsManager.resetToDefaults()
    _uiState.update {
      it.copy(
        adminPin = com.example.util.AdminCredentialsManager.DEFAULT_PIN,
        adminPassword = com.example.util.AdminCredentialsManager.DEFAULT_PASSWORD,
        adminLockMode = com.example.util.AdminLockMode.PIN,
        snackbarMessage = "Admin credentials reset to defaults (PIN: 8888, Password: Admin@1234)"
      )
    }
  }

  fun setColorTheme(theme: AppColorTheme) {
    _uiState.update {
      it.copy(
        activeColorTheme = theme,
        snackbarMessage = "Theme updated: ${theme.displayName}"
      )
    }
  }

  fun setDarkModePreference(pref: DarkModePreference) {
    _uiState.update {
      it.copy(
        darkModePreference = pref,
        snackbarMessage = "Display mode set to ${pref.displayName}"
      )
    }
  }

  fun selectDrug(drug: Drug) {
    val defaultRegimen = drug.defaultRegimen
    val defaultFormulation = drug.formulations.firstOrNull() ?: drug.formulations.first()
    _uiState.update {
      it.copy(
        selectedDrug = drug,
        selectedRegimen = defaultRegimen,
        selectedFormulation = defaultFormulation,
        isCustomDoseActive = false,
        customMgPerKgInput = "",
        courseDurationDays = defaultRegimen.standardDurationDays
      )
    }
    recalculateDose()
  }

  fun viewDrugDetails(drug: Drug) {
    _uiState.update {
      it.copy(
        detailDrug = drug,
        currentTab = AppTab.DRUG_DETAILS
      )
    }
  }

  fun selectDrugAndOpenCalculator(drug: Drug) {
    selectDrug(drug)
    _uiState.update { it.copy(currentTab = AppTab.CALCULATOR) }
  }

  fun setAgeYears(years: Float) {
    val sanitized = years.coerceIn(0f, 18f)
    _uiState.update { it.copy(patientAgeYears = sanitized) }
    recalculateDose()
  }

  fun setAgeMonths(months: Int) {
    val sanitized = months.coerceIn(0, 11)
    _uiState.update { it.copy(patientAgeMonths = sanitized) }
    recalculateDose()
  }

  fun setQuickAgePreset(years: Float, months: Int = 0) {
    _uiState.update {
      it.copy(
        patientAgeYears = years,
        patientAgeMonths = months
      )
    }
    autoEstimateWeightFromAge()
  }

  fun setWeightInput(weightText: String) {
    _uiState.update { it.copy(patientWeightInput = weightText) }
    recalculateDose()
  }

  fun toggleWeightUnit() {
    val currentIsLbs = _uiState.value.isWeightInLbs
    val currentWeight = _uiState.value.patientWeightInput.toDoubleOrNull() ?: 18.5
    
    val convertedWeight = if (currentIsLbs) {
      // lbs to kg
      currentWeight * 0.45359237
    } else {
      // kg to lbs
      currentWeight / 0.45359237
    }

    val formatted = String.format("%.1f", convertedWeight)
    _uiState.update {
      it.copy(
        isWeightInLbs = !currentIsLbs,
        patientWeightInput = formatted
      )
    }
    recalculateDose()
  }

  fun autoEstimateWeightFromAge() {
    val state = _uiState.value
    val estKg = DrugDatabase.estimateWeightKg(state.patientAgeYears, state.patientAgeMonths)
    val displayWeight = if (state.isWeightInLbs) estKg / 0.45359237 else estKg
    val formatted = String.format("%.1f", displayWeight)
    _uiState.update { it.copy(patientWeightInput = formatted) }
    recalculateDose()
  }

  fun selectFormulation(formulation: Formulation) {
    _uiState.update { it.copy(selectedFormulation = formulation) }
    recalculateDose()
  }

  fun selectRegimen(regimen: IndicationRegimen) {
    _uiState.update {
      it.copy(
        selectedRegimen = regimen,
        courseDurationDays = regimen.standardDurationDays
      )
    }
    recalculateDose()
  }

  fun setCustomDose(active: Boolean, mgPerKg: String = "") {
    _uiState.update {
      it.copy(
        isCustomDoseActive = active,
        customMgPerKgInput = mgPerKg
      )
    }
    recalculateDose()
  }

  fun setCourseDurationDays(days: Int) {
    val sanitized = days.coerceIn(1, 30)
    _uiState.update { it.copy(courseDurationDays = sanitized) }
    recalculateDose()
  }

  fun setSearchQuery(query: String) {
    _uiState.update { it.copy(searchQuery = query) }
  }

  fun setCategoryFilter(category: DrugCategory) {
    _uiState.update { it.copy(selectedCategory = category) }
  }

  fun setPatientName(name: String) {
    _uiState.update { it.copy(patientNameInput = name) }
  }

  fun clearSnackbar() {
    _uiState.update { it.copy(snackbarMessage = null) }
  }

  // --- ADMIN & DRUG MANAGEMENT ---

  fun startAddDrug() {
    _uiState.update {
      it.copy(
        isAddingOrEditingDrug = true,
        drugBeingEdited = null
      )
    }
  }

  fun startEditDrug(drug: Drug) {
    _uiState.update {
      it.copy(
        isAddingOrEditingDrug = true,
        drugBeingEdited = drug
      )
    }
  }

  fun closeAddEditDrug() {
    _uiState.update {
      it.copy(
        isAddingOrEditingDrug = false,
        drugBeingEdited = null
      )
    }
  }

  fun saveDrug(drug: Drug) {
    val currentList = _uiState.value.drugList.toMutableList()
    val existingIndex = currentList.indexOfFirst { it.id == drug.id }

    if (existingIndex >= 0) {
      currentList[existingIndex] = drug.copy(lastEditedTimestamp = System.currentTimeMillis())
    } else {
      currentList.add(0, drug.copy(isCustom = true, lastEditedTimestamp = System.currentTimeMillis()))
    }

    _uiState.update {
      it.copy(
        drugList = currentList,
        isAddingOrEditingDrug = false,
        drugBeingEdited = null,
        selectedDrug = if (it.selectedDrug.id == drug.id) drug else it.selectedDrug,
        detailDrug = if (it.detailDrug.id == drug.id) drug else it.detailDrug,
        snackbarMessage = "Drug \"${drug.name}\" saved to Formulary"
      )
    }
    recalculateDose()
  }

  fun deleteDrug(drugId: String) {
    val currentList = _uiState.value.drugList.filterNot { it.id == drugId }
    val nextSelected = currentList.firstOrNull() ?: DrugDatabase.drugs.first()

    _uiState.update {
      it.copy(
        drugList = currentList,
        selectedDrug = if (it.selectedDrug.id == drugId) nextSelected else it.selectedDrug,
        detailDrug = if (it.detailDrug.id == drugId) nextSelected else it.detailDrug,
        currentTab = if (it.detailDrug.id == drugId && it.currentTab == AppTab.DRUG_DETAILS) AppTab.DRUGS else it.currentTab,
        snackbarMessage = "Drug deleted from Formulary"
      )
    }
    recalculateDose()
  }

  fun resetFormularyToDefaults() {
    _uiState.update {
      it.copy(
        drugList = DrugDatabase.drugs,
        selectedDrug = DrugDatabase.drugs.first(),
        selectedRegimen = DrugDatabase.drugs.first().defaultRegimen,
        selectedFormulation = DrugDatabase.drugs.first().formulations.first(),
        snackbarMessage = "Formulary restored to standard default medications"
      )
    }
    recalculateDose()
  }

  // --- CALCULATION CORE ---

  fun recalculateDose() {
    val state = _uiState.value
    val drug = state.selectedDrug
    val regimen = state.selectedRegimen
    val formulation = state.selectedFormulation

    val rawWeight = state.patientWeightInput.toDoubleOrNull() ?: 0.0
    val weightKg = if (state.isWeightInLbs) rawWeight * 0.45359237 else rawWeight

    if (weightKg <= 0.0) {
      _uiState.update { it.copy(calculationResult = null) }
      return
    }

    val totalAgeMonths = (state.patientAgeYears * 12 + state.patientAgeMonths).toInt()
    val isBelowMinAge = totalAgeMonths < drug.minAgeMonths

    val warnings = mutableListOf<String>()
    if (isBelowMinAge) {
      warnings.add("Caution: Patient age ($totalAgeMonths months) is below recommended minimum age (${drug.minAgeMonths} months) for ${drug.name}.")
    }

    // Determine target mg/kg
    val isPerDose = regimen.defaultMgPerKgPerDose != null
    val targetMgPerKg = if (state.isCustomDoseActive && state.customMgPerKgInput.toDoubleOrNull() != null) {
      state.customMgPerKgInput.toDouble()
    } else if (isPerDose) {
      regimen.defaultMgPerKgPerDose ?: 15.0
    } else {
      regimen.defaultMgPerKgPerDay
    }

    val freqTimes = regimen.frequencyTimesPerDay.coerceAtLeast(1)

    // Calculate raw doses
    val rawSingleDoseMg: Double
    val rawDailyDoseMg: Double

    if (isPerDose) {
      rawSingleDoseMg = targetMgPerKg * weightKg
      rawDailyDoseMg = rawSingleDoseMg * freqTimes
    } else {
      rawDailyDoseMg = targetMgPerKg * weightKg
      rawSingleDoseMg = rawDailyDoseMg / freqTimes
    }

    // Cap at max limits if specified
    val isExceedingSingle = rawSingleDoseMg > regimen.maxSingleDoseMg
    val isExceedingDaily = rawDailyDoseMg > regimen.maxDailyDoseMg

    val cappedSingleDoseMg = minOf(rawSingleDoseMg, regimen.maxSingleDoseMg)
    val cappedDailyDoseMg = minOf(rawDailyDoseMg, regimen.maxDailyDoseMg)

    if (isExceedingSingle) {
      warnings.add("Calculated single dose (${String.format("%.1f", rawSingleDoseMg)} mg) exceeds maximum recommended single dose (${regimen.maxSingleDoseMg.toInt()} mg). Capped to maximum.")
    }
    if (isExceedingDaily && !isExceedingSingle) {
      warnings.add("Calculated total daily dose (${String.format("%.1f", rawDailyDoseMg)} mg) exceeds maximum recommended daily limit (${regimen.maxDailyDoseMg.toInt()} mg). Capped to maximum.")
    }

    // Liquid conversion vs Tablet conversion
    val singleDoseMl: Double?
    val totalDailyDoseMl: Double?
    val singleDoseTablets: Double?
    val totalCourseVolumeMl: Double?
    val bottlesSummary: String?

    if (formulation.type == FormulationType.TABLET || formulation.type == FormulationType.CAPSULE) {
      singleDoseMl = null
      totalDailyDoseMl = null
      totalCourseVolumeMl = null
      bottlesSummary = null
      singleDoseTablets = cappedSingleDoseMg / formulation.concentrationMg
    } else {
      singleDoseTablets = null
      val mgPerMl = formulation.mgPerMl
      val singleMl = if (mgPerMl > 0) cappedSingleDoseMg / mgPerMl else 0.0
      val dailyMl = singleMl * freqTimes
      val courseVol = dailyMl * state.courseDurationDays

      singleDoseMl = singleMl
      totalDailyDoseMl = dailyMl
      totalCourseVolumeMl = courseVol

      val bottleSize = formulation.bottleSizesMl.firstOrNull() ?: 100.0
      val bottlesCount = ceil(courseVol / bottleSize).toInt().coerceAtLeast(1)
      bottlesSummary = "${String.format("%.1f", courseVol)} mL total ($bottlesCount × ${bottleSize.toInt()} mL ${if (bottlesCount > 1) "bottles" else "bottle"})"
    }

    // Formulate transparent step-by-step mathematical explanation
    val explanationBuilder = StringBuilder()
    explanationBuilder.append("1. Patient Weight: ${String.format("%.1f", weightKg)} kg\n")
    if (isPerDose) {
      explanationBuilder.append("2. Dose target: ${String.format("%.1f", targetMgPerKg)} mg/kg per dose × ${String.format("%.1f", weightKg)} kg = ${String.format("%.1f", rawSingleDoseMg)} mg/dose\n")
      explanationBuilder.append("3. Daily Total (${freqTimes}× daily): ${String.format("%.1f", rawDailyDoseMg)} mg/day\n")
    } else {
      explanationBuilder.append("2. Daily target: ${String.format("%.1f", targetMgPerKg)} mg/kg/day × ${String.format("%.1f", weightKg)} kg = ${String.format("%.1f", rawDailyDoseMg)} mg/day\n")
      explanationBuilder.append("3. Single dose divided ${freqTimes} times: ${String.format("%.1f", rawDailyDoseMg)} ÷ $freqTimes = ${String.format("%.1f", rawSingleDoseMg)} mg/dose\n")
    }

    if (formulation.type != FormulationType.TABLET && formulation.type != FormulationType.CAPSULE) {
      explanationBuilder.append("4. Liquid concentration: ${formulation.concentrationMg.toInt()} mg / ${formulation.volumeMl.toInt()} mL (${String.format("%.2f", formulation.mgPerMl)} mg/mL)\n")
      explanationBuilder.append("5. Volume per dose: ${String.format("%.1f", cappedSingleDoseMg)} mg ÷ ${String.format("%.2f", formulation.mgPerMl)} mg/mL = ${String.format("%.2f", singleDoseMl ?: 0.0)} mL")
    } else {
      explanationBuilder.append("4. Tablet strength: ${formulation.concentrationMg.toInt()} mg\n")
      explanationBuilder.append("5. Tablets per dose: ${String.format("%.1f", cappedSingleDoseMg)} mg ÷ ${formulation.concentrationMg.toInt()} mg = ${String.format("%.2f", singleDoseTablets ?: 0.0)} tab")
    }

    val result = DoseCalculationResult(
      drug = drug,
      patientAgeYears = state.patientAgeYears,
      patientAgeMonths = state.patientAgeMonths,
      patientWeightKg = weightKg,
      formulation = formulation,
      regimen = regimen,
      mgPerKgUsed = targetMgPerKg,
      isPerDoseCalculation = isPerDose,
      singleDoseMg = cappedSingleDoseMg,
      singleDoseMl = singleDoseMl,
      singleDoseTablets = singleDoseTablets,
      totalDailyDoseMg = cappedDailyDoseMg,
      totalDailyDoseMl = totalDailyDoseMl,
      frequencyText = regimen.frequencyDescription,
      frequencyTimesPerDay = freqTimes,
      courseDurationDays = state.courseDurationDays,
      totalCourseVolumeMl = totalCourseVolumeMl,
      bottlesNeededSummary = bottlesSummary,
      isExceedingMaxSingleDose = isExceedingSingle,
      isExceedingMaxDailyDose = isExceedingDaily,
      isBelowMinAge = isBelowMinAge,
      rawCalculatedSingleDoseMg = rawSingleDoseMg,
      rawCalculatedDailyDoseMg = rawDailyDoseMg,
      maxSingleDoseMg = regimen.maxSingleDoseMg,
      maxDailyDoseMg = regimen.maxDailyDoseMg,
      cappedSingleDoseMg = cappedSingleDoseMg,
      cappedDailyDoseMg = cappedDailyDoseMg,
      warnings = warnings,
      stepByStepExplanation = explanationBuilder.toString()
    )

    _uiState.update { it.copy(calculationResult = result) }
  }

  fun saveCurrentCalculation() {
    val result = _uiState.value.calculationResult ?: return
    val state = _uiState.value

    val ageString = if (state.patientAgeYears >= 1f) {
      if (state.patientAgeMonths > 0) "${state.patientAgeYears.toInt()}y ${state.patientAgeMonths}m" else "${state.patientAgeYears.toInt()} Years"
    } else {
      "${state.patientAgeMonths} Months"
    }

    val singleDoseFormatted = if (result.singleDoseMl != null) {
      "${String.format("%.1f", result.singleDoseMl)} mL (${String.format("%.0f", result.singleDoseMg)} mg)"
    } else {
      "${String.format("%.2f", result.singleDoseTablets ?: 1.0)} tab (${String.format("%.0f", result.singleDoseMg)} mg)"
    }

    val totalDailyFormatted = if (result.totalDailyDoseMl != null) {
      "${String.format("%.1f", result.totalDailyDoseMl)} mL/day (${String.format("%.0f", result.totalDailyDoseMg)} mg/day)"
    } else {
      "${String.format("%.0f", result.totalDailyDoseMg)} mg/day"
    }

    val item = CalculationHistoryItem(
      id = UUID.randomUUID().toString(),
      timestamp = System.currentTimeMillis(),
      patientNameOrId = state.patientNameInput.ifBlank { "Patient (${String.format("%.1f", result.patientWeightKg)} kg)" },
      patientAgeText = ageString,
      patientWeightKg = result.patientWeightKg,
      drugName = result.drug.name,
      formulationText = result.formulation.name,
      singleDoseText = singleDoseFormatted,
      frequencyText = result.frequencyText,
      totalDailyText = totalDailyFormatted,
      durationDays = result.courseDurationDays,
      courseTotalText = result.bottlesNeededSummary ?: "${result.courseDurationDays} days course"
    )

    _uiState.update {
      it.copy(
        history = listOf(item) + it.history,
        snackbarMessage = "Dosage prescription saved to History"
      )
    }
  }

  fun deleteHistoryItem(id: String) {
    _uiState.update {
      it.copy(
        history = it.history.filterNot { item -> item.id == id },
        snackbarMessage = "Entry removed"
      )
    }
  }

  fun clearAllHistory() {
    _uiState.update {
      it.copy(
        history = emptyList(),
        snackbarMessage = "History cleared"
      )
    }
  }
}
