package com.example.ui.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DoseCalculationResult
import com.example.ui.theme.VibrantOnPrimaryContainer
import com.example.ui.theme.VibrantPrimary
import com.example.ui.theme.VibrantPrimaryContainer
import com.example.ui.theme.VibrantWarning
import com.example.ui.theme.VibrantWarningContainer
import com.example.util.PdfReportGenerator
import com.example.viewmodel.AppTab

@Composable
fun AppBottomNavigationBar(
  currentTab: AppTab,
  onTabSelected: (AppTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    color = Color(0xFFF3EDF7),
    tonalElevation = 4.dp,
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(vertical = 6.dp, horizontal = 4.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      NavTabItem(
        label = "Dose",
        selectedIcon = Icons.Filled.Calculate,
        unselectedIcon = Icons.Outlined.Calculate,
        isSelected = currentTab == AppTab.CALCULATOR,
        testTag = "tab_calculator",
        onClick = { onTabSelected(AppTab.CALCULATOR) }
      )

      NavTabItem(
        label = "Drugs",
        selectedIcon = Icons.Filled.Medication,
        unselectedIcon = Icons.Outlined.Medication,
        isSelected = currentTab == AppTab.DRUGS || currentTab == AppTab.DRUG_DETAILS,
        testTag = "tab_drugs",
        onClick = { onTabSelected(AppTab.DRUGS) }
      )

      NavTabItem(
        label = "History",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        isSelected = currentTab == AppTab.HISTORY,
        testTag = "tab_history",
        onClick = { onTabSelected(AppTab.HISTORY) }
      )

      NavTabItem(
        label = "Reference",
        selectedIcon = Icons.Filled.Straighten,
        unselectedIcon = Icons.Outlined.Straighten,
        isSelected = currentTab == AppTab.TOOLS,
        testTag = "tab_tools",
        onClick = { onTabSelected(AppTab.TOOLS) }
      )

      NavTabItem(
        label = "Admin",
        selectedIcon = Icons.Filled.AdminPanelSettings,
        unselectedIcon = Icons.Outlined.AdminPanelSettings,
        isSelected = currentTab == AppTab.ADMIN,
        testTag = "tab_admin",
        onClick = { onTabSelected(AppTab.ADMIN) }
      )
    }
  }
}

@Composable
private fun NavTabItem(
  label: String,
  selectedIcon: ImageVector,
  unselectedIcon: ImageVector,
  isSelected: Boolean,
  testTag: String,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .padding(horizontal = 6.dp, vertical = 2.dp)
      .testTag(testTag)
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(if (isSelected) Color(0xFFE8DEF8) else Color.Transparent)
        .padding(horizontal = 14.dp, vertical = 4.dp)
    ) {
      Icon(
        imageVector = if (isSelected) selectedIcon else unselectedIcon,
        contentDescription = label,
        tint = if (isSelected) VibrantPrimary else Color(0xFF49454F),
        modifier = Modifier.size(22.dp)
      )
    }
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) Color(0xFF1D1B20) else Color(0xFF49454F)
    )
  }
}

@Composable
fun VibrantPill(
  text: String,
  backgroundColor: Color = VibrantPrimaryContainer,
  textColor: Color = VibrantOnPrimaryContainer,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(50))
      .background(backgroundColor)
      .padding(horizontal = 12.dp, vertical = 4.dp)
  ) {
    Text(
      text = text,
      color = textColor,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold
    )
  }
}

@Composable
fun SafetyWarningBanner(
  warning: String,
  modifier: Modifier = Modifier
) {
  Surface(
    color = VibrantWarningContainer,
    shape = RoundedCornerShape(16.dp),
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.Top
    ) {
      Icon(
        imageVector = Icons.Default.Warning,
        contentDescription = "Warning Alert",
        tint = VibrantWarning,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(10.dp))
      Text(
        text = warning,
        color = VibrantWarning,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp
      )
    }
  }
}

/**
 * High-Contrast Critical Validation Layer Banner
 * Displayed when calculated dosage exceeds maximum single/daily dose safety threshold
 */
@Composable
fun HighContrastSafetyThresholdBanner(
  drugName: String,
  calculatedSingleDoseMg: Double,
  maxSingleDoseMg: Double,
  calculatedDailyDoseMg: Double,
  maxDailyDoseMg: Double,
  isSingleExceeded: Boolean,
  isDailyExceeded: Boolean,
  isAgeBelowMinimum: Boolean = false,
  minAgeMonths: Int = 0,
  modifier: Modifier = Modifier
) {
  Surface(
    color = Color(0xFF601410), // High-contrast deep red background
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(2.dp, Color(0xFFFFB4AB)),
    shadowElevation = 4.dp,
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp)
      .testTag("high_contrast_safety_warning_banner")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header with High Contrast Badging
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFFFB4AB)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Dangerous,
              contentDescription = "Safety Alert",
              tint = Color(0xFF601410),
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "SAFETY THRESHOLD EXCEEDED",
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 0.8.sp
            )
            Text(
              text = "Clinical dose validation alert for $drugName",
              color = Color(0xFFFFDAD6),
              fontSize = 11.sp
            )
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFFFDAD6))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "SAFETY CLAMPED",
            color = Color(0xFF410002),
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Comparison Metrics Badges
      if (isSingleExceeded) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF3F0001))
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Calculated Raw Single Dose",
              color = Color(0xFFFFB4AB),
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "${String.format("%.1f", calculatedSingleDoseMg)} mg",
              color = Color(0xFFFFDAD6),
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = "➔",
            color = Color(0xFFFFB4AB),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "Max Safe Ceiling",
              color = Color(0xFFFFB4AB),
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "${maxSingleDoseMg.toInt()} mg (Capped)",
              color = Color(0xFF98EE99), // High visibility soft green accent
              fontSize = 16.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        }
      }

      if (isDailyExceeded && !isSingleExceeded) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF3F0001))
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Calculated 24h Daily Dose",
              color = Color(0xFFFFB4AB),
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "${String.format("%.1f", calculatedDailyDoseMg)} mg/day",
              color = Color(0xFFFFDAD6),
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = "➔",
            color = Color(0xFFFFB4AB),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "Max 24h Threshold",
              color = Color(0xFFFFB4AB),
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "${maxDailyDoseMg.toInt()} mg/day (Capped)",
              color = Color(0xFF98EE99),
              fontSize = 16.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        }
      }

      if (isAgeBelowMinimum) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF3F0001))
            .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.ReportProblem,
            contentDescription = null,
            tint = Color(0xFFFFB4AB),
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Patient age is below the recommended minimum age threshold ($minAgeMonths months).",
            color = Color(0xFFFFDAD6),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Clear clinical advice
      Text(
        text = "⚡ Automated Clinical Safety Action: The application has clamped the calculated prescription to the standard safety ceiling (${if (isSingleExceeded) "${maxSingleDoseMg.toInt()} mg per dose" else "${maxDailyDoseMg.toInt()} mg/day"}) to prevent accidental toxicity.",
        color = Color(0xFFFFDAD6),
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal
      )
    }
  }
}

/**
 * Clinical PDF Export & Print Dialog for Medical Professionals
 */
@Composable
fun PdfExportModalDialog(
  result: DoseCalculationResult,
  defaultPatientName: String = "",
  patientAgeText: String = "",
  patientWeightText: String = "",
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var patientNameInput by remember { mutableStateOf(defaultPatientName) }
  var clinicianNotesInput by remember { mutableStateOf("") }
  var isExporting by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(VibrantPrimaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.PictureAsPdf,
            contentDescription = null,
            tint = VibrantPrimary,
            modifier = Modifier.size(22.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "Export Dosage PDF",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1B20)
          )
          Text(
            text = "Clinical prescription & math record",
            fontSize = 11.sp,
            color = Color(0xFF49454F)
          )
        }
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Surface(
          color = Color(0xFFF3EDF7),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(
              text = "Medication: ${result.drug.name} (${result.formulation.name})",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = VibrantPrimary
            )
            Text(
              text = "Calculated Dose: ${if (result.singleDoseMl != null) "${String.format("%.1f", result.singleDoseMl)} mL (${String.format("%.1f", result.singleDoseMg)} mg)" else "${String.format("%.1f", result.singleDoseMg)} mg"} - ${result.frequencyText}",
              fontSize = 11.sp,
              color = Color(0xFF1D1B20),
              modifier = Modifier.padding(top = 2.dp)
            )
          }
        }

        OutlinedTextField(
          value = patientNameInput,
          onValueChange = { patientNameInput = it },
          label = { Text("Patient Name / Hospital MRN (Optional)") },
          placeholder = { Text("e.g. John Doe / MRN-10492") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_pdf_patient_name")
        )

        OutlinedTextField(
          value = clinicianNotesInput,
          onValueChange = { clinicianNotesInput = it },
          label = { Text("Prescriber Notes / Clinical Instructions") },
          placeholder = { Text("e.g. Administer after meals. Complete full 7-day course.") },
          minLines = 2,
          maxLines = 3,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_pdf_notes")
        )
      }
    },
    confirmButton = {
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Print / View PDF
        OutlinedButton(
          onClick = {
            val file = PdfReportGenerator.generateDosagePdf(
              context = context,
              result = result,
              patientName = patientNameInput,
              patientAgeText = patientAgeText,
              patientWeightText = patientWeightText,
              prescriberNotes = clinicianNotesInput
            )
            PdfReportGenerator.viewOrPrintPdf(context, file)
            onDismiss()
          },
          modifier = Modifier.testTag("btn_pdf_print_view")
        ) {
          Icon(
            imageVector = Icons.Default.Print,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text("Print / View", fontSize = 12.sp)
        }

        // Share PDF
        Button(
          onClick = {
            val file = PdfReportGenerator.generateDosagePdf(
              context = context,
              result = result,
              patientName = patientNameInput,
              patientAgeText = patientAgeText,
              patientWeightText = patientWeightText,
              prescriberNotes = clinicianNotesInput
            )
            PdfReportGenerator.sharePdf(
              context = context,
              pdfFile = file,
              subject = "Pediatric Dosage Record: ${result.drug.name} (${if (patientNameInput.isNotBlank()) patientNameInput else "Pediatric Patient"})"
            )
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary),
          modifier = Modifier.testTag("btn_pdf_share")
        ) {
          Icon(
            imageVector = Icons.Default.Share,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text("Share PDF", fontSize = 12.sp, color = Color.White)
        }
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
