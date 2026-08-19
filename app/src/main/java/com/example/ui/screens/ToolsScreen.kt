package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VibrantDoseCard
import com.example.ui.theme.VibrantDoseText
import com.example.ui.theme.VibrantOutlineVariant
import com.example.ui.theme.VibrantPrimary
import com.example.ui.theme.VibrantPrimaryContainer
import com.example.ui.theme.VibrantSurface

@Composable
fun ToolsScreen(
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  // Custom Universal Calculator states
  var customDrugName by remember { mutableStateOf("Custom Medicine") }
  var customWeight by remember { mutableStateOf("20.0") }
  var customMgPerKg by remember { mutableStateOf("15.0") }
  var customStrengthMg by remember { mutableStateOf("250.0") }
  var customStrengthMl by remember { mutableStateOf("5.0") }
  var customFreqTimes by remember { mutableStateOf("3") }

  // Calculation computation
  val weightVal = customWeight.toDoubleOrNull() ?: 20.0
  val mgPerKgVal = customMgPerKg.toDoubleOrNull() ?: 15.0
  val strMg = customStrengthMg.toDoubleOrNull() ?: 250.0
  val strMl = customStrengthMl.toDoubleOrNull() ?: 5.0
  val freqVal = customFreqTimes.toIntOrNull() ?: 3

  val mgPerMl = if (strMl > 0) strMg / strMl else 50.0
  val singleDoseMg = weightVal * mgPerKgVal
  val singleDoseMl = if (mgPerMl > 0) singleDoseMg / mgPerMl else 0.0
  val dailyDoseMg = singleDoseMg * freqVal
  val dailyDoseMl = singleDoseMl * freqVal

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F2FA))
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(VibrantPrimaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Straighten,
          contentDescription = null,
          tint = VibrantPrimary,
          modifier = Modifier.size(22.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = "Clinical Reference & Estimators",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1D1B20)
        )
        Text(
          text = "Universal dilution calculator & pediatric formulas",
          fontSize = 12.sp,
          color = Color(0xFF49454F)
        )
      }
    }

    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(scrollState),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Universal Custom Dose Calculator
      Surface(
        color = VibrantSurface,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Science, contentDescription = null, tint = VibrantPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Universal Liquid Dilution Calculator",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1D1B20)
            )
          }
          Text(
            text = "Calculate exact mL for any unlisted syrup or suspension",
            fontSize = 12.sp,
            color = Color(0xFF49454F),
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
          )

          // Inputs
          OutlinedTextField(
            value = customDrugName,
            onValueChange = { customDrugName = it },
            label = { Text("Drug Name") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = customWeight,
              onValueChange = { customWeight = it },
              label = { Text("Weight (kg)") },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f),
              singleLine = true
            )

            OutlinedTextField(
              value = customMgPerKg,
              onValueChange = { customMgPerKg = it },
              label = { Text("mg / kg / dose") },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f),
              singleLine = true
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = customStrengthMg,
              onValueChange = { customStrengthMg = it },
              label = { Text("Strength (mg)") },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f),
              singleLine = true
            )

            OutlinedTextField(
              value = customStrengthMl,
              onValueChange = { customStrengthMl = it },
              label = { Text("in Volume (mL)") },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f),
              singleLine = true
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Result box
          Surface(
            color = VibrantDoseCard,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "CALCULATED DOSE VOLUME",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = VibrantDoseText
              )
              Row(verticalAlignment = Alignment.Bottom) {
                Text(
                  text = String.format("%.2f", singleDoseMl),
                  fontSize = 32.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = VibrantDoseText
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "mL / dose",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = VibrantDoseText,
                  modifier = Modifier.padding(bottom = 4.dp)
                )
              }
              Text(
                text = "= ${String.format("%.1f", singleDoseMg)} mg per single dose",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF381E72)
              )
              Text(
                text = "Daily Total (3x): ${String.format("%.2f", dailyDoseMl)} mL/day (${String.format("%.1f", dailyDoseMg)} mg)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF21005D),
                modifier = Modifier.padding(top = 4.dp)
              )
            }
          }
        }
      }

      // 2. Pediatric Weight Estimation Reference
      Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE6E0E9)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Pediatric Weight Estimation Rules (APLS / Nelson)",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1B20)
          )

          Spacer(modifier = Modifier.height(10.dp))
          ReferenceRow(age = "Infant (1–11 months)", formula = "Weight (kg) = (Months + 9) ÷ 2", sample = "6 mos ≈ 7.5 kg")
          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFECE6F0))
          ReferenceRow(age = "Child (1–5 years)", formula = "Weight (kg) = 2 × (Age in Years + 5)", sample = "3 yrs ≈ 16 kg")
          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFECE6F0))
          ReferenceRow(age = "Child (6–12 years)", formula = "Weight (kg) = (3 × Age in Years) + 7", sample = "7 yrs ≈ 28 kg")
        }
      }

      // 3. Clinical Pearls Card
      Surface(
        color = VibrantSurface,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Safe Pediatric Administration Pearls",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = VibrantPrimary
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "• Always use calibrated oral syringes or medicine spoons rather than household teaspoons, which vary between 2.5 mL and 8 mL.",
            fontSize = 12.sp,
            color = Color(0xFF49454F),
            lineHeight = 16.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "• Never exceed adult maximum doses regardless of pediatric weight calculations.",
            fontSize = 12.sp,
            color = Color(0xFF49454F),
            lineHeight = 16.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "• For obese children, calculate hydrophilic drugs (e.g. Aminoglycosides, Paracetamol) on ideal body weight to prevent toxicity.",
            fontSize = 12.sp,
            color = Color(0xFF49454F),
            lineHeight = 16.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun ReferenceRow(age: String, formula: String, sample: String) {
  Column {
    Text(text = age, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VibrantPrimary)
    Text(text = formula, fontSize = 12.sp, color = Color(0xFF1D1B20), fontWeight = FontWeight.Medium)
    Text(text = "Example: $sample", fontSize = 11.sp, color = Color(0xFF79747E))
  }
}
