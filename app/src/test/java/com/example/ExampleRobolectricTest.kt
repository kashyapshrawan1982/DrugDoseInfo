package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DrugDatabase
import com.example.model.Drug
import com.example.model.DrugCategory
import com.example.model.Formulation
import com.example.model.FormulationType
import com.example.model.IndicationRegimen
import com.example.ui.theme.AppColorTheme
import com.example.ui.theme.DarkModePreference
import com.example.viewmodel.AppTab
import com.example.viewmodel.DoseCalculatorViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Before
  fun setUp() {
    com.example.util.AdminCredentialsManager.resetToDefaults()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Dose Calculator", appName)
  }

  @Test
  fun `verify drug database has essential drugs`() {
    val amox = DrugDatabase.findDrugById("amoxicillin")
    assertNotNull(amox)
    assertEquals("Amoxicillin", amox?.name)
    assertEquals(DrugCategory.ANTIBIOTIC, amox?.category)

    val pcm = DrugDatabase.findDrugById("paracetamol")
    assertNotNull(pcm)

    val estWeight = DrugDatabase.estimateWeightKg(5.0f, 0)
    assertTrue(estWeight in 15.0..25.0)
  }

  @Test
  fun `verify admin PIN lock and authentication security`() {
    val viewModel = DoseCalculatorViewModel()

    // 1. Initial state must be locked
    assertFalse(viewModel.uiState.value.isAdminAuthenticated)

    // 2. Reject incorrect PIN
    val wrongAttempt = viewModel.authenticateAdmin("0000")
    assertFalse(wrongAttempt)
    assertFalse(viewModel.uiState.value.isAdminAuthenticated)

    // 3. Grant access with default master PIN "8888"
    val correctAttempt = viewModel.authenticateAdmin("8888")
    assertTrue(correctAttempt)
    assertTrue(viewModel.uiState.value.isAdminAuthenticated)

    // 4. Test changing PIN
    val changeSuccess = viewModel.changeAdminPin("8888", "5678")
    assertTrue(changeSuccess)
    assertEquals("5678", viewModel.uiState.value.adminPin)

    // 5. Test locking/logout
    viewModel.logoutAdmin()
    assertFalse(viewModel.uiState.value.isAdminAuthenticated)

    // 6. Old PIN must fail, new PIN must succeed
    assertFalse(viewModel.authenticateAdmin("8888"))
    assertTrue(viewModel.authenticateAdmin("5678"))
    assertTrue(viewModel.uiState.value.isAdminAuthenticated)
  }

  @Test
  fun `verify admin adding, editing and deleting custom drug`() {
    val viewModel = DoseCalculatorViewModel()
    val initialCount = viewModel.uiState.value.drugList.size

    // Authenticate admin first
    viewModel.authenticateAdmin("8888")
    assertTrue(viewModel.uiState.value.isAdminAuthenticated)

    // 1. Add new custom drug
    val customDrug = Drug(
      id = "custom_test_drug_${UUID.randomUUID()}",
      name = "Cefdinir",
      genericName = "Cefdinir Omnicef",
      category = DrugCategory.ANTIBIOTIC,
      subtitle = "3rd Generation Cephalosporin",
      description = "Broad spectrum antibiotic for otitis media and sinusitis",
      standardRegimenSummary = "14 mg/kg/day divided q12-24h",
      adultDoseSummary = "300 mg q12h or 600 mg q24h",
      minAgeMonths = 6,
      defaultRegimen = IndicationRegimen(
        id = "cefdinir_std",
        name = "Standard AOM / RTI",
        description = "14 mg/kg/day once daily or divided BID",
        defaultMgPerKgPerDay = 14.0,
        frequencyTimesPerDay = 1,
        frequencyDescription = "Once daily (or 7 mg/kg every 12 hours)",
        maxDailyDoseMg = 600.0,
        maxSingleDoseMg = 600.0,
        standardDurationDays = 10
      ),
      formulations = listOf(
        Formulation("cefdinir_125", "Suspension 125 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 125.0, 5.0, "125mg/5mL"),
        Formulation("cefdinir_250", "Suspension 250 mg / 5 mL", FormulationType.ORAL_SUSPENSION, 250.0, 5.0, "250mg/5mL")
      ),
      indications = listOf("Acute Otitis Media", "Community Acquired Pneumonia"),
      contraindications = listOf("Severe cephalosporin allergy"),
      warnings = listOf("Reddish stools when combined with iron"),
      sideEffects = listOf("Diarrhea", "Headache"),
      administrationAdvice = "Can take without regard to food.",
      isCustom = true
    )

    viewModel.saveDrug(customDrug)
    assertEquals(initialCount + 1, viewModel.uiState.value.drugList.size)
    val added = viewModel.uiState.value.drugList.find { it.id == customDrug.id }
    assertNotNull(added)
    assertTrue(added?.isCustom == true)

    // 2. Edit the custom drug
    val modifiedDrug = customDrug.copy(
      name = "Cefdinir Forte",
      subtitle = "Updated Subtitle"
    )
    viewModel.saveDrug(modifiedDrug)
    assertEquals(initialCount + 1, viewModel.uiState.value.drugList.size)
    val updated = viewModel.uiState.value.drugList.find { it.id == customDrug.id }
    assertEquals("Cefdinir Forte", updated?.name)

    // 3. Delete the drug
    viewModel.deleteDrug(customDrug.id)
    assertEquals(initialCount, viewModel.uiState.value.drugList.size)
    val deleted = viewModel.uiState.value.drugList.find { it.id == customDrug.id }
    assertTrue(deleted == null)
  }

  @Test
  fun `verify UI theme customization and display mode options`() {
    val viewModel = DoseCalculatorViewModel()

    // Test Color Theme Switch
    viewModel.setColorTheme(AppColorTheme.CLINICAL_TEAL)
    assertEquals(AppColorTheme.CLINICAL_TEAL, viewModel.uiState.value.activeColorTheme)

    viewModel.setColorTheme(AppColorTheme.ROYAL_OCEAN)
    assertEquals(AppColorTheme.ROYAL_OCEAN, viewModel.uiState.value.activeColorTheme)

    // Test Dark Mode Preference
    viewModel.setDarkModePreference(DarkModePreference.DARK)
    assertEquals(DarkModePreference.DARK, viewModel.uiState.value.darkModePreference)

    viewModel.setDarkModePreference(DarkModePreference.LIGHT)
    assertEquals(DarkModePreference.LIGHT, viewModel.uiState.value.darkModePreference)
  }

  @Test
  fun `verify reset formulary restores default medications`() {
    val viewModel = DoseCalculatorViewModel()
    val initialCount = DrugDatabase.drugs.size

    // Delete a drug
    val firstDrug = viewModel.uiState.value.drugList.first()
    viewModel.deleteDrug(firstDrug.id)
    assertEquals(initialCount - 1, viewModel.uiState.value.drugList.size)

    // Reset to defaults
    viewModel.resetFormularyToDefaults()
    assertEquals(initialCount, viewModel.uiState.value.drugList.size)
  }

  @Test
  fun `verify safety threshold validation and high dose warning layer`() {
    val viewModel = DoseCalculatorViewModel()
    val paracetamol = DrugDatabase.findDrugById("paracetamol")
    assertNotNull(paracetamol)
    viewModel.selectDrug(paracetamol!!)

    // Test with excessive weight (e.g. 90 kg child or high custom dose)
    // Paracetamol max single dose is 1000 mg (15 mg/kg * 90 kg = 1350 mg > 1000 mg max)
    viewModel.setWeightInput("90.0")
    viewModel.recalculateDose()

    val result = viewModel.uiState.value.calculationResult
    assertNotNull(result)
    assertTrue("Should trigger safety threshold alert", result?.hasSafetyThresholdAlert == true)
    assertTrue("Should exceed max single dose limit", result?.isExceedingMaxSingleDose == true)
    assertEquals(1000.0, result?.cappedSingleDoseMg ?: 0.0, 0.01)
    assertTrue((result?.rawCalculatedSingleDoseMg ?: 0.0) > 1000.0)
    assertTrue("Warnings list should contain maximum threshold notice", result?.warnings?.any { it.contains("exceeds maximum", ignoreCase = true) } == true)
  }

  @Test
  fun `verify PDF summary generation creates valid PDF file`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val viewModel = DoseCalculatorViewModel()
    val amox = DrugDatabase.findDrugById("amoxicillin")
    assertNotNull(amox)
    viewModel.selectDrug(amox!!)
    viewModel.setWeightInput("15.0")
    viewModel.recalculateDose()

    val result = viewModel.uiState.value.calculationResult
    assertNotNull(result)

    val pdfFile = com.example.util.PdfReportGenerator.generateDosagePdf(
      context = context,
      result = result!!,
      patientName = "Test Patient",
      patientAgeText = "3 yrs",
      patientWeightText = "15.0 kg",
      prescriberNotes = "Test clinical notes"
    )

    assertNotNull(pdfFile)
    assertTrue("PDF file should exist", pdfFile.exists())
    assertTrue("PDF file should have positive byte size", pdfFile.length() > 0)
    assertTrue("PDF file should have .pdf extension", pdfFile.name.endsWith(".pdf"))
  }

  @Test
  fun `verify admin can change pin and password and authenticate with both`() {
    val viewModel = DoseCalculatorViewModel()

    // 1. Authenticate using default password "Admin@1234"
    val pwdLogin = viewModel.authenticateAdmin("Admin@1234")
    assertTrue("Should authenticate with default password", pwdLogin)
    assertTrue(viewModel.uiState.value.isAdminAuthenticated)

    // 2. Change admin password
    val changePasswordResult = viewModel.changeAdminPassword("Admin@1234", "Clinical@2026Secure")
    assertTrue("Password change should succeed", changePasswordResult)
    assertEquals("Clinical@2026Secure", viewModel.uiState.value.adminPassword)

    // 3. Change admin PIN using current password as verification
    val changePinResult = viewModel.changeAdminPin("Clinical@2026Secure", "9988")
    assertTrue("PIN change should succeed", changePinResult)
    assertEquals("9988", viewModel.uiState.value.adminPin)

    // 4. Logout admin session
    viewModel.logoutAdmin()
    assertFalse(viewModel.uiState.value.isAdminAuthenticated)

    // 5. Old PIN/Password should fail
    assertFalse(viewModel.authenticateAdmin("8888"))
    assertFalse(viewModel.authenticateAdmin("Admin@1234"))

    // 6. New PIN and new Password should both succeed
    assertTrue(viewModel.authenticateAdmin("9988"))
    assertTrue(viewModel.uiState.value.isAdminAuthenticated)

    viewModel.logoutAdmin()
    assertTrue(viewModel.authenticateAdmin("Clinical@2026Secure"))
    assertTrue(viewModel.uiState.value.isAdminAuthenticated)

    // 7. Reset to defaults
    viewModel.resetAdminCredentialsToDefaults()
    assertEquals("8888", viewModel.uiState.value.adminPin)
    assertEquals("Admin@1234", viewModel.uiState.value.adminPassword)
  }

  @Test
  fun `verify password strength evaluation utility`() {
    val weak = com.example.util.AdminCredentialsManager.assessPasswordStrength("123")
    assertEquals(com.example.util.PasswordStrength.WEAK, weak)

    val moderate = com.example.util.AdminCredentialsManager.assessPasswordStrength("SimplePass")
    assertEquals(com.example.util.PasswordStrength.MODERATE, moderate)

    val strong = com.example.util.AdminCredentialsManager.assessPasswordStrength("P3d1@tr1c#2026")
    assertEquals(com.example.util.PasswordStrength.STRONG, strong)
  }

  @Test
  fun `verify drug models contain clinical references and citations`() {
    val amox = DrugDatabase.findDrugById("amoxicillin")
    assertNotNull(amox)
    assertTrue("Amoxicillin should contain clinical references", amox?.references?.isNotEmpty() == true)
    assertTrue("References should mention AAP or BNF-C", amox?.references?.any { it.contains("AAP") || it.contains("BNF-C") } == true)

    val pcm = DrugDatabase.findDrugById("paracetamol")
    assertNotNull(pcm)
    assertTrue("Paracetamol should contain clinical references", pcm?.references?.isNotEmpty() == true)
  }
}
