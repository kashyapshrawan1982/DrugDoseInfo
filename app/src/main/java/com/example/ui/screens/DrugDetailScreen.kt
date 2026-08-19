package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Drug
import com.example.ui.components.VibrantPill
import com.example.ui.theme.VibrantOutlineVariant
import com.example.ui.theme.VibrantPrimary
import com.example.ui.theme.VibrantPrimaryContainer
import com.example.ui.theme.VibrantSurface
import com.example.ui.theme.VibrantWarning
import com.example.ui.theme.VibrantWarningContainer

@Composable
fun DrugDetailScreen(
  drug: Drug,
  onBack: () -> Unit,
  onCalculateDose: (Drug) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F2FA))
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_from_detail")) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = VibrantPrimary)
      }
      Spacer(modifier = Modifier.width(4.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = drug.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1B20)
          )
          if (drug.isCustom) {
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFD4F7DC))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "CUSTOM",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F5132)
              )
            }
          }
        }
        Text(
          text = drug.subtitle,
          fontSize = 12.sp,
          color = Color(0xFF49454F)
        )
      }
    }

    // Scrollable Content
    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(scrollState)
        .padding(horizontal = 16.dp, vertical = 4.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Overview Card
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
              text = "GENERIC NAME",
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.sp,
              color = VibrantPrimary
            )
            VibrantPill(text = drug.category.displayName)
          }

          Text(
            text = drug.genericName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1B20),
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
          )

          Text(
            text = drug.description,
            fontSize = 13.sp,
            color = Color(0xFF49454F),
            lineHeight = 18.sp
          )
        }
      }

      // 2. Dosing Guidelines Card
      Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE6E0E9)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Dosing & Administration Guidelines",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1B20)
          )

          Spacer(modifier = Modifier.height(10.dp))
          // Pediatric
          Text(
            text = "Pediatric Weight-Based Regimen",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = VibrantPrimary
          )
          Text(
            text = drug.standardRegimenSummary,
            fontSize = 13.sp,
            color = Color(0xFF49454F),
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
          )

          HorizontalDivider(color = Color(0xFFECE6F0))
          Spacer(modifier = Modifier.height(10.dp))

          // Adult
          Text(
            text = "Adult Fixed Dosing",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = VibrantPrimary
          )
          Text(
            text = drug.adultDoseSummary,
            fontSize = 13.sp,
            color = Color(0xFF49454F),
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }

      // 3. Available Formulations & Strengths
      Surface(
        color = VibrantSurface,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Available Formulations & Concentrations",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1B20)
          )
          Spacer(modifier = Modifier.height(8.dp))

          drug.formulations.forEach { form ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .clip(CircleShape)
                  .background(VibrantPrimary)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "${form.name} (${String.format("%.2f", form.mgPerMl)} mg/mL)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1D1B20)
              )
            }
          }

          drug.reconstitutionStorage?.let { storage ->
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = VibrantOutlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Storage & Reconstitution",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = VibrantPrimary
            )
            Text(
              text = storage,
              fontSize = 12.sp,
              color = Color(0xFF49454F),
              lineHeight = 16.sp
            )
          }
        }
      }

      // 4. Indications
      Surface(
        color = VibrantSurface,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Clinical Indications",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1B20)
          )
          Spacer(modifier = Modifier.height(8.dp))

          drug.indications.forEach { ind ->
            Row(
              modifier = Modifier.padding(vertical = 2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF1B873F),
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(text = ind, fontSize = 13.sp, color = Color(0xFF1D1B20))
            }
          }
        }
      }

      // 5. Contraindications & Safety Warnings
      Surface(
        color = VibrantWarningContainer,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = VibrantWarning)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Contraindications & Precautions",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = VibrantWarning
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          drug.contraindications.forEach { contra ->
            Text(
              text = "• $contra",
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              color = VibrantWarning,
              modifier = Modifier.padding(vertical = 2.dp)
            )
          }

          drug.warnings.forEach { warn ->
            Text(
              text = "• $warn",
              fontSize = 12.sp,
              color = Color(0xFF601410),
              modifier = Modifier.padding(vertical = 2.dp)
            )
          }
        }
      }

      // 6. Side Effects & Administration Advice
      Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE6E0E9)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Administration & Side Effects",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1B20)
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Advice: ${drug.administrationAdvice}",
            fontSize = 13.sp,
            color = Color(0xFF49454F),
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Common Adverse Effects: ${drug.sideEffects.joinToString(", ")}",
            fontSize = 12.sp,
            color = Color(0xFF625B71)
          )

          drug.renalAdjustmentNote?.let { renal ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Renal Adjustment: $renal",
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF7D5260)
            )
          }
        }
      }

      // 7. Clinical References & Evidence-Based Guidelines
      Surface(
        color = Color(0xFFF3EDF7),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEADDFF)),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("card_drug_references")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.MenuBook,
              contentDescription = null,
              tint = VibrantPrimary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Clinical References & Guidelines",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1D1B20)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          val displayReferences = if (drug.references.isNotEmpty()) {
            drug.references
          } else {
            listOf(
              "American Academy of Pediatrics (AAP) Pediatric Clinical Practice Guidelines.",
              "British National Formulary for Children (BNF-C) 2025–2026 Edition.",
              "Nelson Textbook of Pediatrics, 21st Edition (Elsevier).",
              "WHO Model Formulary for Children & Model List of Essential Medicines (EMLc).",
              "Lexicomp Pediatric & Neonatal Dosage Handbook with International Guidelines."
            )
          }

          displayReferences.forEachIndexed { index, ref ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
              verticalAlignment = Alignment.Top
            ) {
              Box(
                modifier = Modifier
                  .padding(top = 2.dp)
                  .size(18.dp)
                  .clip(CircleShape)
                  .background(VibrantPrimaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${index + 1}",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF21005D)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = ref,
                fontSize = 12.sp,
                color = Color(0xFF49454F),
                lineHeight = 17.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          HorizontalDivider(color = Color(0xFFEADDFF), thickness = 0.8.dp)
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Peer-reviewed prescribing standards. Cross-check against local institutional formularies.",
            fontSize = 10.5.sp,
            color = Color(0xFF79747E),
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Action Buttons Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp)
      ) {
        Button(
          onClick = { onCalculateDose(drug) },
          shape = RoundedCornerShape(50),
          colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("btn_calculate_from_detail")
        ) {
          Icon(imageVector = Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Calculate Pediatric Dose", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
