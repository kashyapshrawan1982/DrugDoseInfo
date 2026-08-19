package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Drug
import com.example.model.DrugCategory
import com.example.model.Formulation
import com.example.model.FormulationType
import com.example.model.IndicationRegimen
import com.example.ui.theme.VibrantOutlineVariant
import com.example.ui.theme.VibrantPrimary
import com.example.ui.theme.VibrantPrimaryContainer
import com.example.ui.theme.VibrantSurface
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDrugScreen(
  existingDrug: Drug?,
  onSave: (Drug) -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  // Basic Info
  var name by remember { mutableStateOf(existingDrug?.name ?: "") }
  var genericName by remember { mutableStateOf(existingDrug?.genericName ?: "") }
  var selectedCategory by remember { mutableStateOf(existingDrug?.category ?: DrugCategory.ANTIBIOTIC) }
  var categoryExpanded by remember { mutableStateOf(false) }
  var subtitle by remember { mutableStateOf(existingDrug?.subtitle ?: "") }
  var description by remember { mutableStateOf(existingDrug?.description ?: "") }
  var minAgeMonthsStr by remember { mutableStateOf(existingDrug?.minAgeMonths?.toString() ?: "1") }

  // Regimen details
  var regimenName by remember { mutableStateOf(existingDrug?.defaultRegimen?.name ?: "Standard Pediatric Dosing") }
  var regimenDesc by remember { mutableStateOf(existingDrug?.defaultRegimen?.description ?: "Weight-based regimen") }
  var isPerDose by remember { mutableStateOf(existingDrug?.defaultRegimen?.defaultMgPerKgPerDose != null) }
  var targetMgPerKgStr by remember {
    mutableStateOf(
      existingDrug?.let {
        if (it.defaultRegimen.defaultMgPerKgPerDose != null) {
          it.defaultRegimen.defaultMgPerKgPerDose.toString()
        } else {
          it.defaultRegimen.defaultMgPerKgPerDay.toString()
        }
      } ?: "30.0"
    )
  }
  var frequencyTimesStr by remember { mutableStateOf(existingDrug?.defaultRegimen?.frequencyTimesPerDay?.toString() ?: "3") }
  var frequencyDesc by remember { mutableStateOf(existingDrug?.defaultRegimen?.frequencyDescription ?: "Every 8 hours (3 times daily)") }
  var maxSingleDoseStr by remember { mutableStateOf(existingDrug?.defaultRegimen?.maxSingleDoseMg?.toString() ?: "500.0") }
  var maxDailyDoseStr by remember { mutableStateOf(existingDrug?.defaultRegimen?.maxDailyDoseMg?.toString() ?: "1500.0") }
  var standardDaysStr by remember { mutableStateOf(existingDrug?.defaultRegimen?.standardDurationDays?.toString() ?: "7") }

  // Formulations
  var formulations by remember {
    mutableStateOf(
      existingDrug?.formulations ?: listOf(
        Formulation(
          id = UUID.randomUUID().toString(),
          name = "Oral Suspension 125 mg / 5 mL",
          type = FormulationType.ORAL_SUSPENSION,
          concentrationMg = 125.0,
          volumeMl = 5.0,
          unitLabel = "125mg/5mL"
        )
      )
    )
  }

  // Temporary Formulation Adder
  var showAddFormulationModal by remember { mutableStateOf(false) }
  var newFormName by remember { mutableStateOf("") }
  var newFormConcStr by remember { mutableStateOf("250.0") }
  var newFormVolStr by remember { mutableStateOf("5.0") }
  var newFormType by remember { mutableStateOf(FormulationType.ORAL_SUSPENSION) }
  var formTypeExpanded by remember { mutableStateOf(false) }

  // Clinical monograph items
  var indicationsText by remember { mutableStateOf(existingDrug?.indications?.joinToString("\n") ?: "Acute infection\nFever / Pain") }
  var contraindicationsText by remember { mutableStateOf(existingDrug?.contraindications?.joinToString("\n") ?: "Known hypersensitivity") }
  var warningsText by remember { mutableStateOf(existingDrug?.warnings?.joinToString("\n") ?: "Adjust dose in severe renal impairment") }
  var sideEffectsText by remember { mutableStateOf(existingDrug?.sideEffects?.joinToString(", ") ?: "Nausea, Diarrhea, Mild rash") }
  var adminAdvice by remember { mutableStateOf(existingDrug?.administrationAdvice ?: "Administer with water or food.") }
  var storageText by remember { mutableStateOf(existingDrug?.reconstitutionStorage ?: "Store at controlled room temperature 20°C to 25°C.") }
  var adultDoseText by remember { mutableStateOf(existingDrug?.adultDoseSummary ?: "500 mg PO every 8 hours.") }

  var validationError by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F2FA))
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onCancel, modifier = Modifier.testTag("btn_cancel_add_drug")) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Cancel", tint = VibrantPrimary)
      }
      Spacer(modifier = Modifier.width(4.dp))
      Column {
        Text(
          text = if (existingDrug != null) "Edit Medication" else "Add New Drug to Formulary",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1D1B20)
        )
        Text(
          text = if (existingDrug != null) "Update clinical rules & formulations" else "Create custom medication with dosage formulas",
          fontSize = 12.sp,
          color = Color(0xFF49454F)
        )
      }
    }

    // Scrollable Form Body
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(scrollState)
        .padding(horizontal = 16.dp, vertical = 4.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Basic Identification Card
      Surface(
        color = VibrantSurface,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "1. Drug Identification",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = VibrantPrimary
          )
          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Brand / Common Name (e.g. Amoxicillin)*") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_drug_name"),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = genericName,
            onValueChange = { genericName = it },
            label = { Text("Generic / Chemical Name*") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Category Dropdown
          ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded },
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedTextField(
              value = selectedCategory.displayName,
              onValueChange = {},
              readOnly = true,
              label = { Text("Therapeutic Category") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
            )
            ExposedDropdownMenu(
              expanded = categoryExpanded,
              onDismissRequest = { categoryExpanded = false }
            ) {
              DrugCategory.values().filterNot { it == DrugCategory.ALL }.forEach { cat ->
                DropdownMenuItem(
                  text = { Text(cat.displayName) },
                  onClick = {
                    selectedCategory = cat
                    categoryExpanded = false
                  }
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = subtitle,
            onValueChange = { subtitle = it },
            label = { Text("Subtitle / Short Class (e.g. Broad-Spectrum Antibiotic)") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Clinical Description & Mechanism") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = minAgeMonthsStr,
            onValueChange = { minAgeMonthsStr = it },
            label = { Text("Minimum Safe Age (Months)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
        }
      }

      // 2. Regimen & Calculation Formulas Card
      Surface(
        color = VibrantSurface,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "2. Pediatric Dosing Formula & Limits",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = VibrantPrimary
          )
          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = regimenName,
            onValueChange = { regimenName = it },
            label = { Text("Regimen Name (e.g. Standard Infection)") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Calculation mode toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (!isPerDose) VibrantPrimary else Color(0xFFE8DEF8))
                .clickable { isPerDose = false }
                .padding(vertical = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "mg / kg / DAY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (!isPerDose) Color.White else Color(0xFF1D1B20)
              )
            }

            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isPerDose) VibrantPrimary else Color(0xFFE8DEF8))
                .clickable { isPerDose = true }
                .padding(vertical = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "mg / kg / DOSE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPerDose) Color.White else Color(0xFF1D1B20)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = targetMgPerKgStr,
              onValueChange = { targetMgPerKgStr = it },
              label = { Text(if (isPerDose) "mg/kg/dose" else "mg/kg/day") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f),
              singleLine = true
            )

            OutlinedTextField(
              value = frequencyTimesStr,
              onValueChange = { frequencyTimesStr = it },
              label = { Text("Doses / Day") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f),
              singleLine = true
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = frequencyDesc,
            onValueChange = { frequencyDesc = it },
            label = { Text("Frequency Description (e.g. Every 8 hours with food)") },
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
              value = maxSingleDoseStr,
              onValueChange = { maxSingleDoseStr = it },
              label = { Text("Max Single (mg)") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f),
              singleLine = true
            )

            OutlinedTextField(
              value = maxDailyDoseStr,
              onValueChange = { maxDailyDoseStr = it },
              label = { Text("Max 24h (mg)") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(1f),
              singleLine = true
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = standardDaysStr,
            onValueChange = { standardDaysStr = it },
            label = { Text("Standard Course Duration (Days)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
        }
      }

      // 3. Formulations & Concentrations Card
      Surface(
        color = VibrantSurface,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "3. Formulations (${formulations.size})",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = VibrantPrimary
            )

            Button(
              onClick = { showAddFormulationModal = true },
              shape = RoundedCornerShape(50),
              colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimaryContainer),
              modifier = Modifier.height(34.dp)
            ) {
              Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color(0xFF21005D), modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add Strength", fontSize = 11.sp, color = Color(0xFF21005D), fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          formulations.forEachIndexed { index, form ->
            Surface(
              color = Color(0xFFF3EDF7),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(text = form.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                  Text(
                    text = "${form.concentrationMg.toInt()} mg in ${form.volumeMl.toInt()} mL (${String.format("%.2f", form.mgPerMl)} mg/mL)",
                    fontSize = 11.sp,
                    color = Color(0xFF49454F)
                  )
                }

                if (formulations.size > 1) {
                  IconButton(
                    onClick = {
                      formulations = formulations.filterIndexed { i, _ -> i != index }
                    },
                    modifier = Modifier.size(32.dp)
                  ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFB3261E), modifier = Modifier.size(18.dp))
                  }
                }
              }
            }
          }

          // Inline formulation adder if toggled
          if (showAddFormulationModal) {
            Surface(
              color = Color.White,
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6750A4)),
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Add New Formulation / Strength", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = VibrantPrimary)
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                  value = newFormName,
                  onValueChange = { newFormName = it },
                  label = { Text("Label (e.g. Oral Suspension 250mg/5mL)") },
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.fillMaxWidth(),
                  singleLine = true
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  OutlinedTextField(
                    value = newFormConcStr,
                    onValueChange = { newFormConcStr = it },
                    label = { Text("Strength (mg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                  )

                  OutlinedTextField(
                    value = newFormVolStr,
                    onValueChange = { newFormVolStr = it },
                    label = { Text("Volume (mL)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                  )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.End,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  OutlinedButton(
                    onClick = { showAddFormulationModal = false },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.height(36.dp)
                  ) {
                    Text("Cancel", fontSize = 11.sp)
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  Button(
                    onClick = {
                      val c = newFormConcStr.toDoubleOrNull() ?: 125.0
                      val v = newFormVolStr.toDoubleOrNull() ?: 5.0
                      val label = if (newFormName.isNotBlank()) newFormName else "${c.toInt()}mg / ${v.toInt()}mL"
                      val newF = Formulation(
                        id = UUID.randomUUID().toString(),
                        name = label,
                        type = newFormType,
                        concentrationMg = c,
                        volumeMl = v,
                        unitLabel = "${c.toInt()}mg/${v.toInt()}mL"
                      )
                      formulations = formulations + newF
                      newFormName = ""
                      showAddFormulationModal = false
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary),
                    modifier = Modifier.height(36.dp)
                  ) {
                    Text("Add", fontSize = 11.sp)
                  }
                }
              }
            }
          }
        }
      }

      // 4. Clinical Details & Monograph Card
      Surface(
        color = VibrantSurface,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "4. Clinical Monograph & Safety Advice",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = VibrantPrimary
          )
          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = indicationsText,
            onValueChange = { indicationsText = it },
            label = { Text("Indications (one per line)") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = contraindicationsText,
            onValueChange = { contraindicationsText = it },
            label = { Text("Contraindications (one per line)") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = warningsText,
            onValueChange = { warningsText = it },
            label = { Text("Safety Warnings & Precautions") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = sideEffectsText,
            onValueChange = { sideEffectsText = it },
            label = { Text("Common Side Effects") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = adminAdvice,
            onValueChange = { adminAdvice = it },
            label = { Text("Administration Advice") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = storageText,
            onValueChange = { storageText = it },
            label = { Text("Storage & Reconstitution Instructions") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = adultDoseText,
            onValueChange = { adultDoseText = it },
            label = { Text("Adult Fixed Dosing Summary") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
        }
      }

      // Validation Error if any
      validationError?.let { err ->
        Text(
          text = err,
          color = Color(0xFFB3261E),
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(horizontal = 4.dp)
        )
      }

      // Save & Cancel Actions
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedButton(
          onClick = onCancel,
          shape = RoundedCornerShape(50),
          modifier = Modifier
            .weight(1f)
            .height(50.dp)
        ) {
          Text("Cancel", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = {
            if (name.isBlank()) {
              validationError = "Please enter a drug name."
              return@Button
            }
            val targetMg = targetMgPerKgStr.toDoubleOrNull()
            if (targetMg == null || targetMg <= 0.0) {
              validationError = "Please enter a valid mg/kg dosage number."
              return@Button
            }

            val freqT = frequencyTimesStr.toIntOrNull() ?: 3
            val maxSingle = maxSingleDoseStr.toDoubleOrNull() ?: 1000.0
            val maxDaily = maxDailyDoseStr.toDoubleOrNull() ?: 3000.0
            val minAge = minAgeMonthsStr.toIntOrNull() ?: 1
            val days = standardDaysStr.toIntOrNull() ?: 7

            val defaultReg = IndicationRegimen(
              id = existingDrug?.defaultRegimen?.id ?: UUID.randomUUID().toString(),
              name = regimenName.ifBlank { "Standard Regimen" },
              description = regimenDesc.ifBlank { "Weight-based regimen" },
              defaultMgPerKgPerDay = if (isPerDose) targetMg * freqT else targetMg,
              defaultMgPerKgPerDose = if (isPerDose) targetMg else null,
              frequencyTimesPerDay = freqT,
              frequencyDescription = frequencyDesc.ifBlank { "Every ${24 / freqT} hours" },
              maxDailyDoseMg = maxDaily,
              maxSingleDoseMg = maxSingle,
              minAgeMonths = minAge,
              standardDurationDays = days
            )

            val newDrug = Drug(
              id = existingDrug?.id ?: "custom_${name.lowercase().replace(" ", "_")}_${UUID.randomUUID().toString().take(6)}",
              name = name.trim(),
              genericName = genericName.ifBlank { name }.trim(),
              category = selectedCategory,
              subtitle = subtitle.ifBlank { "${selectedCategory.displayName} agent" }.trim(),
              description = description.ifBlank { "Custom medication entry." }.trim(),
              standardRegimenSummary = "${targetMg.toInt()} mg/kg ${if (isPerDose) "per dose" else "per day"} divided $frequencyDesc. Max $maxDaily mg/day.",
              adultDoseSummary = adultDoseText.ifBlank { "Standard adult dosage." },
              minAgeMonths = minAge,
              defaultRegimen = defaultReg,
              alternativeRegimens = existingDrug?.alternativeRegimens ?: emptyList(),
              formulations = formulations.ifEmpty {
                listOf(
                  Formulation(
                    id = UUID.randomUUID().toString(),
                    name = "Standard Formulation",
                    type = FormulationType.ORAL_SUSPENSION,
                    concentrationMg = 125.0,
                    volumeMl = 5.0
                  )
                )
              },
              indications = indicationsText.lines().map { it.trim() }.filter { it.isNotBlank() },
              contraindications = contraindicationsText.lines().map { it.trim() }.filter { it.isNotBlank() },
              warnings = warningsText.lines().map { it.trim() }.filter { it.isNotBlank() },
              sideEffects = sideEffectsText.split(",").map { it.trim() }.filter { it.isNotBlank() },
              administrationAdvice = adminAdvice.trim(),
              reconstitutionStorage = storageText.trim(),
              isCustom = true,
              lastEditedTimestamp = System.currentTimeMillis()
            )

            onSave(newDrug)
          },
          shape = RoundedCornerShape(50),
          colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary),
          modifier = Modifier
            .weight(1.5f)
            .height(50.dp)
            .testTag("btn_save_drug")
        ) {
          Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Save to Formulary", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
