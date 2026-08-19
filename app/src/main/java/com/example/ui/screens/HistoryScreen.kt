package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationHistoryItem
import com.example.ui.theme.VibrantOutlineVariant
import com.example.ui.theme.VibrantPrimary
import com.example.ui.theme.VibrantPrimaryContainer
import com.example.ui.theme.VibrantSurface
import com.example.util.PdfReportGenerator
import com.example.viewmodel.DoseCalculatorUiState
import com.example.viewmodel.DoseCalculatorViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
  state: DoseCalculatorUiState,
  viewModel: DoseCalculatorViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var showClearConfirmDialog by remember { mutableStateOf(false) }

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
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(VibrantPrimaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = VibrantPrimary,
            modifier = Modifier.size(22.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "Prescription History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1B20)
          )
          Text(
            text = "${state.history.size} saved calculations",
            fontSize = 12.sp,
            color = Color(0xFF49454F)
          )
        }
      }

      if (state.history.isNotEmpty()) {
        IconButton(
          onClick = { showClearConfirmDialog = true },
          modifier = Modifier.testTag("btn_clear_history")
        ) {
          Icon(
            imageVector = Icons.Default.DeleteSweep,
            contentDescription = "Clear all history",
            tint = Color(0xFFB3261E)
          )
        }
      }
    }

    if (state.history.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = Color(0xFF79747E),
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No saved dose calculations yet",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF49454F)
          )
          Text(
            text = "Calculate a dose and tap 'Save Dose' to record prescriptions here.",
            fontSize = 12.sp,
            color = Color(0xFF79747E),
            modifier = Modifier.padding(top = 4.dp, start = 32.dp, end = 32.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(state.history, key = { it.id }) { item ->
          HistoryItemCard(
            item = item,
            onShare = {
              val shareText = """
                |💊 *Dose Calculation Prescription Note*
                |Drug: ${item.drugName}
                |Strength: ${item.formulationText}
                |Patient: ${item.patientNameOrId} • ${item.patientAgeText} (${item.patientWeightKg} kg)
                |Prescribed Dose: ${item.singleDoseText}
                |Regimen: ${item.frequencyText}
                |Daily Total: ${item.totalDailyText}
                |Course: ${item.courseTotalText}
              """.trimMargin()

              val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
              }
              context.startActivity(Intent.createChooser(intent, "Share Prescription Note"))
            },
            onDelete = { viewModel.deleteHistoryItem(item.id) }
          )
        }
        item {
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }

  if (showClearConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showClearConfirmDialog = false },
      title = { Text("Clear All History?") },
      text = { Text("This will permanently remove all saved dosage records.") },
      confirmButton = {
        Button(
          onClick = {
            viewModel.clearAllHistory()
            showClearConfirmDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E))
        ) {
          Text("Clear All")
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showClearConfirmDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun HistoryItemCard(
  item: CalculationHistoryItem,
  onShare: () -> Unit,
  onDelete: () -> Unit
) {
  val context = LocalContext.current
  val dateFormatted = remember(item.timestamp) {
    SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(item.timestamp))
  }

  Surface(
    color = VibrantSurface,
    shape = RoundedCornerShape(16.dp),
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
          text = item.drugName,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = VibrantPrimary
        )
        Text(
          text = dateFormatted,
          fontSize = 11.sp,
          color = Color(0xFF79747E)
        )
      }

      Text(
        text = item.formulationText,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF49454F),
        modifier = Modifier.padding(top = 2.dp)
      )

      HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFECE6F0))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(text = "Patient", fontSize = 11.sp, color = Color(0xFF79747E))
          Text(
            text = "${item.patientAgeText} • ${item.patientWeightKg} kg",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1D1B20)
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(text = "Prescribed Single Dose", fontSize = 11.sp, color = Color(0xFF79747E))
          Text(
            text = item.singleDoseText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF21005D)
          )
        }
      }

      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Schedule: ${item.frequencyText} (${item.courseTotalText})",
        fontSize = 12.sp,
        color = Color(0xFF49454F)
      )

      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // PDF Export Button
        IconButton(
          onClick = {
            val file = PdfReportGenerator.generateHistoryItemPdf(context, item)
            PdfReportGenerator.sharePdf(context, file, "Pediatric Dose Record: ${item.drugName} (${item.patientNameOrId})")
          },
          modifier = Modifier
            .size(36.dp)
            .testTag("btn_history_pdf_${item.id.take(4)}")
        ) {
          Icon(
            imageVector = Icons.Default.PictureAsPdf,
            contentDescription = "Export PDF Record",
            tint = Color(0xFF6750A4),
            modifier = Modifier.size(18.dp)
          )
        }

        // Print / View Button
        IconButton(
          onClick = {
            val file = PdfReportGenerator.generateHistoryItemPdf(context, item)
            PdfReportGenerator.viewOrPrintPdf(context, file)
          },
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Print,
            contentDescription = "Print PDF Record",
            tint = Color(0xFF49454F),
            modifier = Modifier.size(18.dp)
          )
        }

        IconButton(onClick = onShare, modifier = Modifier.size(36.dp)) {
          Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = VibrantPrimary, modifier = Modifier.size(18.dp))
        }
        IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
          Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFB3261E), modifier = Modifier.size(18.dp))
        }
      }
    }
  }
}
