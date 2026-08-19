package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import com.example.model.Drug
import com.example.model.Formulation
import com.example.model.FormulationType
import com.example.model.IndicationRegimen
import com.example.ui.components.HighContrastSafetyThresholdBanner
import com.example.ui.components.PdfExportModalDialog
import com.example.ui.components.SafetyWarningBanner
import com.example.ui.components.VibrantPill
import com.example.ui.theme.VibrantDoseCard
import com.example.ui.theme.VibrantDoseSubtext
import com.example.ui.theme.VibrantDoseText
import com.example.ui.theme.VibrantOutline
import com.example.ui.theme.VibrantOutlineVariant
import com.example.ui.theme.VibrantPrimary
import com.example.ui.theme.VibrantPrimaryContainer
import com.example.ui.theme.VibrantSecondaryContainer
import com.example.ui.theme.VibrantSurface
import com.example.viewmodel.DoseCalculatorUiState
import com.example.viewmodel.DoseCalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
  state: DoseCalculatorUiState,
  viewModel: DoseCalculatorViewModel,
  onOpenDrugList: () -> Unit,
  onViewDrugDetail: (Drug) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val focusManager = LocalFocusManager.current
  val scrollState = rememberScrollState()
  var showAgeSheet by remember { mutableStateOf(false) }
  var showFormulationSheet by remember { mutableStateOf(false) }
  var showRegimenSheet by remember { mutableStateOf(false) }
  var showMathDetails by remember { mutableStateOf(false) }
  var showSaveDialog by remember { mutableStateOf(false) }
  var showPdfDialog by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F2FA))
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(VibrantPrimaryContainer)
            .clickable { onOpenDrugList() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Medication,
            contentDescription = "Select Drug",
            tint = VibrantPrimary,
            modifier = Modifier.size(22.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "Dose Calculator",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1B20)
          )
          Text(
            text = "Weight & Age-calibrated precision",
            fontSize = 12.sp,
            color = Color(0xFF49454F)
          )
        }
      }

      Row {
        IconButton(
          onClick = { onViewDrugDetail(state.selectedDrug) },
          modifier = Modifier.testTag("btn_drug_info")
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Drug monograph",
            tint = VibrantPrimary
          )
        }
      }
    }

    // Main Content
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(scrollState)
        .padding(horizontal = 16.dp, vertical = 4.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Drug Banner Card (Matching design mock)
      Surface(
        color = VibrantSurface,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onOpenDrugList() }
          .testTag("drug_banner_card")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = state.selectedDrug.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = VibrantPrimary
              )
              Text(
                text = "${state.selectedDrug.category.displayName} • ${state.selectedFormulation.type.name.replace("_", " ")}",
                fontSize = 13.sp,
                color = Color(0xFF49454F),
                modifier = Modifier.padding(top = 2.dp)
              )
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(VibrantPrimaryContainer)
                .clickable { showFormulationSheet = true }
                .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Text(
                text = state.selectedFormulation.unitLabel,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF21005D)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Tap to switch medication or concentration",
              fontSize = 11.sp,
              color = VibrantPrimary,
              fontWeight = FontWeight.Medium
            )
            Icon(
              imageVector = Icons.Default.ChevronRight,
              contentDescription = null,
              tint = VibrantPrimary,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // 2. Patient Inputs: Age and Body Weight
      // Age Selector Box
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Age of Patient",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = VibrantPrimary,
          modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
        )
        Surface(
          color = Color.Transparent,
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, VibrantPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showAgeSheet = true }
            .testTag("patient_age_field")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            val ageText = if (state.patientAgeYears >= 1f) {
              if (state.patientAgeMonths > 0) "${state.patientAgeYears.toInt()} Years ${state.patientAgeMonths} Months" else "${state.patientAgeYears.toInt()} Years"
            } else {
              "${state.patientAgeMonths} Months"
            }
            Text(
              text = ageText,
              fontSize = 16.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF1D1B20)
            )
            Text(text = "▼", fontSize = 12.sp, color = VibrantPrimary)
          }
        }
      }

      // Body Weight Input Box with stepper & unit toggle
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Body Weight (${if (state.isWeightInLbs) "lbs" else "kg"})",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF49454F),
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
          )

          // Quick estimate button
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .clickable { viewModel.autoEstimateWeightFromAge() }
              .padding(horizontal = 6.dp, vertical = 2.dp)
              .testTag("btn_estimate_weight")
          ) {
            Icon(
              imageVector = Icons.Outlined.ElectricBolt,
              contentDescription = null,
              tint = VibrantPrimary,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Estimate by Age",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = VibrantPrimary
            )
          }
        }

        Surface(
          color = Color.Transparent,
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF79747E)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Decrement Button
            IconButton(
              onClick = {
                val current = state.patientWeightInput.toDoubleOrNull() ?: 18.5
                val updated = maxOf(1.0, current - 0.5)
                viewModel.setWeightInput(String.format("%.1f", updated))
              },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease weight", tint = VibrantPrimary)
            }

            OutlinedTextField(
              value = state.patientWeightInput,
              onValueChange = { viewModel.setWeightInput(it) },
              modifier = Modifier
                .weight(1f)
                .testTag("input_weight"),
              textStyle = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20)
              ),
              keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
              ),
              keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
              ),
              singleLine = true
            )

            // Unit toggle pill
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(VibrantSecondaryContainer)
                .clickable { viewModel.toggleWeightUnit() }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("btn_toggle_unit")
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = if (state.isWeightInLbs) "lbs" else "kg",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1D192B)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                  imageVector = Icons.Default.SwapHoriz,
                  contentDescription = "Switch units",
                  tint = Color(0xFF1D192B),
                  modifier = Modifier.size(14.dp)
                )
              }
            }

            // Increment Button
            IconButton(
              onClick = {
                val current = state.patientWeightInput.toDoubleOrNull() ?: 18.5
                val updated = current + 0.5
                viewModel.setWeightInput(String.format("%.1f", updated))
              },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(imageVector = Icons.Default.Add, contentDescription = "Increase weight", tint = VibrantPrimary)
            }
          }
        }
      }

      // Quick Age & Weight presets bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        val presets = listOf(
          Triple("6 Months", 0.5f, 6),
          Triple("1 Year", 1f, 0),
          Triple("2 Years", 2f, 0),
          Triple("5 Years", 5f, 0),
          Triple("8 Years", 8f, 0),
          Triple("12 Years", 12f, 0),
          Triple("Adult", 18f, 0)
        )
        presets.forEach { (label, yrs, mos) ->
          val isSelected = state.patientAgeYears == yrs
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(16.dp))
              .background(if (isSelected) VibrantPrimary else Color(0xFFE8DEF8))
              .clickable { viewModel.setQuickAgePreset(yrs, mos) }
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = label,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = if (isSelected) Color.White else Color(0xFF1D1B20)
            )
          }
        }
      }

      // 3. Clinical Indication Regimen Selector
      Surface(
        color = VibrantSurface,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { showRegimenSheet = true }
          .testTag("regimen_selector_card")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Indication / Clinical Regimen",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = VibrantPrimary
            )
            Text(
              text = state.selectedRegimen.name,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF1D1B20),
              modifier = Modifier.padding(top = 2.dp)
            )
          }
          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = VibrantPrimary
          )
        }
      }

      // 4. Formulation Selection Chips
      if (state.selectedDrug.formulations.size > 1) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "Formulation Strength",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF49454F),
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
          )
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            state.selectedDrug.formulations.forEach { form ->
              val isSelected = state.selectedFormulation.id == form.id
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) VibrantPrimary else Color(0xFFFEF7FF))
                  .border(
                    1.dp,
                    if (isSelected) VibrantPrimary else VibrantOutlineVariant,
                    RoundedCornerShape(12.dp)
                  )
                  .clickable { viewModel.selectFormulation(form) }
                  .padding(horizontal = 12.dp, vertical = 8.dp)
                  .testTag("formulation_chip_${form.id}")
              ) {
                Text(
                  text = form.unitLabel,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else Color(0xFF1D1B20)
                )
              }
            }
          }
        }
      }

      // 5. CALCULATED SINGLE DOSE CARD (Vibrant Theme Hero)
      state.calculationResult?.let { result ->
        val onShareDosageDetails = {
          val warningsText = if (result.warnings.isNotEmpty()) {
            "\n⚠️ Warnings / Alerts:\n" + result.warnings.joinToString("\n") { "• $it" } + "\n"
          } else ""

          val singleDoseStr = if (result.singleDoseMl != null) {
            "${String.format("%.1f", result.singleDoseMl)} mL (${String.format("%.1f", result.singleDoseMg)} mg)"
          } else if (result.singleDoseTablets != null) {
            "${String.format("%.2f", result.singleDoseTablets)} tablet (${String.format("%.0f", result.singleDoseMg)} mg)"
          } else {
            "${String.format("%.1f", result.singleDoseMg)} mg"
          }

          val totalDailyStr = if (result.totalDailyDoseMl != null) {
            "${String.format("%.1f", result.totalDailyDoseMl)} mL / 24h (${String.format("%.0f", result.totalDailyDoseMg)} mg)"
          } else {
            "${String.format("%.0f", result.totalDailyDoseMg)} mg / 24h"
          }

          val shareText = """
            |📋 *CLINICAL DOSE CALCULATION DETAILS*
            |-------------------------------------------
            |💊 *Drug:* ${result.drug.name} (${result.drug.genericName})
            |🧪 *Formulation:* ${result.formulation.name}
            |🎯 *Indication:* ${result.regimen.name}
            |
            |👤 *Patient Info:*
            |• Weight: ${state.patientWeightInput} ${if (state.isWeightInLbs) "lbs" else "kg"} (${String.format("%.2f", result.patientWeightKg)} kg)
            |• Age: ${if (state.patientAgeYears >= 1f) "${state.patientAgeYears.toInt()} yrs ${if (state.patientAgeMonths > 0) "${state.patientAgeMonths} mos" else ""}" else "${state.patientAgeMonths} months"}
            |
            |💉 *PRESCRIBED DOSE:*
            |👉 *Single Dose:* $singleDoseStr
            |👉 *Frequency:* ${result.frequencyText}
            |👉 *24h Total Dose:* $totalDailyStr
            |👉 *Course Duration:* ${result.courseDurationDays} Days (${result.bottlesNeededSummary ?: "standard regimen"})
            |$warningsText
            |📐 *Formula & Calculation:*
            |${result.stepByStepExplanation}
            |-------------------------------------------
            |Generated by Dose Calculator
          """.trimMargin()

          val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Dosage Details: ${result.drug.name} (${String.format("%.2f", result.patientWeightKg)} kg)")
            putExtra(Intent.EXTRA_TEXT, shareText)
          }
          context.startActivity(Intent.createChooser(shareIntent, "Share Dosage Details"))
        }

        Surface(
          color = VibrantDoseCard,
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("calculated_dose_card")
        ) {
          Box(modifier = Modifier.fillMaxWidth()) {
            // Quick Share Icon on top right corner of hero card
            IconButton(
              onClick = onShareDosageDetails,
              modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .testTag("btn_quick_share_dose")
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share Dosage Details",
                tint = VibrantDoseText,
                modifier = Modifier.size(22.dp)
              )
            }

            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "CALCULATED SINGLE DOSE",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp,
                color = VibrantDoseText
              )

              Spacer(modifier = Modifier.height(6.dp))

              if (result.singleDoseMl != null) {
                Row(verticalAlignment = Alignment.Bottom) {
                  Text(
                    text = String.format("%.1f", result.singleDoseMl),
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = VibrantDoseText,
                    lineHeight = 46.sp
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "mL",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibrantDoseText,
                    modifier = Modifier.padding(bottom = 6.dp)
                  )
                }
                Text(
                  text = "(Approx. ${String.format("%.1f", result.singleDoseMg)} mg per dose)",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium,
                  color = VibrantDoseSubtext.copy(alpha = 0.9f)
                )
              } else if (result.singleDoseTablets != null) {
                Row(verticalAlignment = Alignment.Bottom) {
                  Text(
                    text = String.format("%.2f", result.singleDoseTablets),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = VibrantDoseText
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "tablet",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibrantDoseText,
                    modifier = Modifier.padding(bottom = 6.dp)
                  )
                }
                Text(
                  text = "(${String.format("%.0f", result.singleDoseMg)} mg dose)",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium,
                  color = VibrantDoseSubtext
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Frequency Schedule Pill
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.7f))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = result.frequencyText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VibrantDoseText
                  )
                }

                // Share pill on card
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF21005D))
                    .clickable { onShareDosageDetails() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("share_result_pill")
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Share,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "Share Dose",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color.White
                    )
                  }
                }

                // PDF Export pill on card
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF6750A4))
                    .clickable { showPdfDialog = true }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("pdf_export_result_pill")
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.PictureAsPdf,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "PDF Record",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color.White
                    )
                  }
                }
              }
            }
          }
        }

        // Validation Layer: High-Contrast Safety Threshold Warning Banner
        if (result.isExceedingMaxSingleDose || result.isExceedingMaxDailyDose || result.isBelowMinAge) {
          HighContrastSafetyThresholdBanner(
            drugName = result.drug.name,
            calculatedSingleDoseMg = result.rawCalculatedSingleDoseMg,
            maxSingleDoseMg = result.maxSingleDoseMg,
            calculatedDailyDoseMg = result.rawCalculatedDailyDoseMg,
            maxDailyDoseMg = result.maxDailyDoseMg,
            isSingleExceeded = result.isExceedingMaxSingleDose,
            isDailyExceeded = result.isExceedingMaxDailyDose,
            isAgeBelowMinimum = result.isBelowMinAge,
            minAgeMonths = result.drug.minAgeMonths
          )
        }

        // Additional Standard Clinical Warnings (if any)
        result.warnings.filterNot { 
          // Avoid duplicate text if already displayed by high contrast banner
          it.contains("exceeds maximum", ignoreCase = true)
        }.forEach { warn ->
          SafetyWarningBanner(warning = warn)
        }

        // 6. Regimen & Daily Total Info Card (Matching Design HTML)
        Surface(
          color = Color.White,
          shape = RoundedCornerShape(18.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE6E0E9)),
          shadowElevation = 1.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(bottom = 6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF3B82F6))
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Standard Regimen & Daily Total",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20)
              )
            }

            Text(
              text = "${state.selectedDrug.standardRegimenSummary}",
              fontSize = 13.sp,
              color = Color(0xFF49454F),
              lineHeight = 18.sp
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFECE6F0))

            // Daily & Course Summary
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(text = "Total 24h Dose", fontSize = 11.sp, color = Color(0xFF49454F))
                Text(
                  text = if (result.totalDailyDoseMl != null) {
                    "${String.format("%.1f", result.totalDailyDoseMl)} mL / day (${String.format("%.0f", result.totalDailyDoseMg)} mg)"
                  } else {
                    "${String.format("%.0f", result.totalDailyDoseMg)} mg / day"
                  },
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1D1B20)
                )
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(text = "Course (${result.courseDurationDays} Days)", fontSize = 11.sp, color = Color(0xFF49454F))
                Text(
                  text = result.bottlesNeededSummary ?: "${result.courseDurationDays} days duration",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = VibrantPrimary
                )
              }
            }
          }
        }

        // 7. Course Duration Selector Bar
        Surface(
          color = VibrantSurface,
          shape = RoundedCornerShape(14.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Course Duration",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20)
              )
              Text(
                text = "${state.courseDurationDays} Days",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = VibrantPrimary
              )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              listOf(3, 5, 7, 10, 14).forEach { days ->
                val isSelected = state.courseDurationDays == days
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) VibrantPrimary else Color(0xFFE8DEF8))
                    .clickable { viewModel.setCourseDurationDays(days) }
                    .padding(vertical = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "${days}d",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color(0xFF1D1B20)
                  )
                }
              }
            }
          }
        }

        // 8. Expandable Step-by-Step Calculation Breakdown
        Surface(
          color = Color(0xFFFEF7FF),
          shape = RoundedCornerShape(14.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showMathDetails = !showMathDetails }
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Functions,
                  contentDescription = null,
                  tint = VibrantPrimary,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Step-by-Step Formula Derivation",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = VibrantPrimary
                )
              }
              Text(
                text = if (showMathDetails) "▲ Hide" else "▼ Show",
                fontSize = 12.sp,
                color = VibrantPrimary,
                fontWeight = FontWeight.Medium
              )
            }

            AnimatedVisibility(visible = showMathDetails) {
              Column(modifier = Modifier.padding(top = 10.dp)) {
                HorizontalDivider(color = VibrantOutlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = result.stepByStepExplanation,
                  fontSize = 12.sp,
                  color = Color(0xFF49454F),
                  lineHeight = 18.sp,
                  fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
              }
            }
          }
        }
      }

      // Bottom Action Buttons: Recalculate, Save & Share
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = { viewModel.recalculateDose() },
          colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary),
          shape = RoundedCornerShape(50),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("btn_recalculate")
        ) {
          Text(
            text = "Recalculate Dose",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          FilledTonalButton(
            onClick = { viewModel.saveCurrentCalculation() },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFFEADDFF)),
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("btn_save_history")
          ) {
            Icon(
              imageVector = Icons.Default.BookmarkBorder,
              contentDescription = null,
              tint = Color(0xFF21005D),
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Save Dose",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF21005D)
            )
          }

          OutlinedButton(
            onClick = {
              val res = state.calculationResult
              if (res != null) {
                val warningsText = if (res.warnings.isNotEmpty()) {
                  "\n⚠️ Warnings / Alerts:\n" + res.warnings.joinToString("\n") { "• $it" } + "\n"
                } else ""

                val singleDoseStr = if (res.singleDoseMl != null) {
                  "${String.format("%.1f", res.singleDoseMl)} mL (${String.format("%.1f", res.singleDoseMg)} mg)"
                } else if (res.singleDoseTablets != null) {
                  "${String.format("%.2f", res.singleDoseTablets)} tablet (${String.format("%.0f", res.singleDoseMg)} mg)"
                } else {
                  "${String.format("%.1f", res.singleDoseMg)} mg"
                }

                val totalDailyStr = if (res.totalDailyDoseMl != null) {
                  "${String.format("%.1f", res.totalDailyDoseMl)} mL / 24h (${String.format("%.0f", res.totalDailyDoseMg)} mg)"
                } else {
                  "${String.format("%.0f", res.totalDailyDoseMg)} mg / 24h"
                }

                val shareText = """
                  |📋 *CLINICAL DOSE CALCULATION DETAILS*
                  |-------------------------------------------
                  |💊 *Drug:* ${res.drug.name} (${res.drug.genericName})
                  |🧪 *Formulation:* ${res.formulation.name}
                  |🎯 *Indication:* ${res.regimen.name}
                  |
                  |👤 *Patient Info:*
                  |• Weight: ${state.patientWeightInput} ${if (state.isWeightInLbs) "lbs" else "kg"} (${String.format("%.2f", res.patientWeightKg)} kg)
                  |• Age: ${if (state.patientAgeYears >= 1f) "${state.patientAgeYears.toInt()} yrs ${if (state.patientAgeMonths > 0) "${state.patientAgeMonths} mos" else ""}" else "${state.patientAgeMonths} months"}
                  |
                  |💉 *PRESCRIBED DOSE:*
                  |👉 *Single Dose:* $singleDoseStr
                  |👉 *Frequency:* ${res.frequencyText}
                  |👉 *24h Total Dose:* $totalDailyStr
                  |👉 *Course Duration:* ${res.courseDurationDays} Days (${res.bottlesNeededSummary ?: "standard regimen"})
                  |$warningsText
                  |📐 *Formula & Calculation:*
                  |${res.stepByStepExplanation}
                  |-------------------------------------------
                  |Generated by Dose Calculator
                """.trimMargin()

                val intent = Intent(Intent.ACTION_SEND).apply {
                  type = "text/plain"
                  putExtra(Intent.EXTRA_SUBJECT, "Dosage Details: ${res.drug.name} (${String.format("%.2f", res.patientWeightKg)} kg)")
                  putExtra(Intent.EXTRA_TEXT, shareText)
                }
                context.startActivity(Intent.createChooser(intent, "Share Dosage Details"))
              }
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("btn_share_dose")
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share Dosage Details",
              tint = VibrantPrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Share",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = VibrantPrimary
            )
          }

          FilledTonalButton(
            onClick = { showPdfDialog = true },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF6750A4)),
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("btn_export_pdf")
          ) {
            Icon(
              imageVector = Icons.Default.PictureAsPdf,
              contentDescription = "Export PDF Record",
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "PDF",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }
  }

  // Clinical PDF Export Dialog
  if (showPdfDialog) {
    state.calculationResult?.let { result ->
      PdfExportModalDialog(
        result = result,
        defaultPatientName = state.patientNameInput,
        patientAgeText = if (state.patientAgeYears >= 1f) "${state.patientAgeYears.toInt()} yrs ${if (state.patientAgeMonths > 0) "${state.patientAgeMonths} mos" else ""}" else "${state.patientAgeMonths} mos",
        patientWeightText = "${state.patientWeightInput} ${if (state.isWeightInLbs) "lbs" else "kg"}",
        onDismiss = { showPdfDialog = false }
      )
    }
  }

  // Bottom Sheet: Age Selection
  if (showAgeSheet) {
    ModalBottomSheet(
      onDismissRequest = { showAgeSheet = false },
      sheetState = rememberModalBottomSheetState()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "Select Patient Age",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1D1B20)
        )
        Text(
          text = "Adjust years and months for accurate pediatric calculation",
          fontSize = 13.sp,
          color = Color(0xFF49454F),
          modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Years: ${state.patientAgeYears.toInt()}", fontWeight = FontWeight.Bold)
        }
        Slider(
          value = state.patientAgeYears,
          onValueChange = { viewModel.setAgeYears(it) },
          valueRange = 0f..18f,
          steps = 17,
          colors = SliderDefaults.colors(thumbColor = VibrantPrimary, activeTrackColor = VibrantPrimary)
        )

        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Months (if under 2 years): ${state.patientAgeMonths}m", fontWeight = FontWeight.Bold)
        }
        Slider(
          value = state.patientAgeMonths.toFloat(),
          onValueChange = { viewModel.setAgeMonths(it.toInt()) },
          valueRange = 0f..11f,
          steps = 10,
          colors = SliderDefaults.colors(thumbColor = VibrantPrimary, activeTrackColor = VibrantPrimary)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = {
            viewModel.autoEstimateWeightFromAge()
            showAgeSheet = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Apply & Estimate Weight")
        }
      }
    }
  }

  // Bottom Sheet: Formulation Selection
  if (showFormulationSheet) {
    ModalBottomSheet(
      onDismissRequest = { showFormulationSheet = false }
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "Available Formulations & Strengths",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1D1B20)
        )
        Spacer(modifier = Modifier.height(12.dp))

        state.selectedDrug.formulations.forEach { form ->
          val isSelected = state.selectedFormulation.id == form.id
          Surface(
            color = if (isSelected) VibrantPrimaryContainer else Color(0xFFF7F2FA),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clickable {
                viewModel.selectFormulation(form)
                showFormulationSheet = false
              }
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = form.name,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = if (isSelected) Color(0xFF21005D) else Color(0xFF1D1B20)
                )
                Text(
                  text = "${form.concentrationMg.toInt()} mg in ${form.volumeMl.toInt()} mL (${String.format("%.2f", form.mgPerMl)} mg/mL)",
                  fontSize = 12.sp,
                  color = Color(0xFF49454F)
                )
              }
              if (isSelected) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF21005D))
              }
            }
          }
        }
      }
    }
  }

  // Bottom Sheet: Regimen Selection
  if (showRegimenSheet) {
    ModalBottomSheet(
      onDismissRequest = { showRegimenSheet = false }
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "Clinical Regimens for ${state.selectedDrug.name}",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1D1B20)
        )
        Spacer(modifier = Modifier.height(12.dp))

        val allRegimens = listOf(state.selectedDrug.defaultRegimen) + state.selectedDrug.alternativeRegimens
        allRegimens.forEach { reg ->
          val isSelected = state.selectedRegimen.id == reg.id
          Surface(
            color = if (isSelected) VibrantPrimaryContainer else Color(0xFFF7F2FA),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clickable {
                viewModel.selectRegimen(reg)
                showRegimenSheet = false
              }
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = reg.name,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = if (isSelected) Color(0xFF21005D) else Color(0xFF1D1B20)
                )
                if (isSelected) {
                  Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF21005D))
                }
              }
              Text(
                text = reg.description,
                fontSize = 12.sp,
                color = Color(0xFF49454F),
                modifier = Modifier.padding(top = 4.dp)
              )
            }
          }
        }
      }
    }
  }
}
