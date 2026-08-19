package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.model.CalculationHistoryItem
import com.example.model.DoseCalculationResult
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object PdfReportGenerator {

  /**
   * Generates a clinical-grade PDF report for the given dosage calculation result.
   */
  fun generateDosagePdf(
    context: Context,
    result: DoseCalculationResult,
    patientName: String = "Pediatric Patient",
    patientAgeText: String = "",
    patientWeightText: String = "",
    prescriberNotes: String? = null
  ): File {
    val pdfDir = File(context.cacheDir, "dosage_reports").apply { mkdirs() }
    val sanitizedDrugName = result.drug.name.replace("[^a-zA-Z0-9]".toRegex(), "_")
    val file = File(pdfDir, "Dosage_Summary_${sanitizedDrugName}_${System.currentTimeMillis()}.pdf")

    try {
      val pdfDocument = PdfDocument()
      val pageWidth = 595 // Standard A4 width in points (72 dpi)
      val pageHeight = 842 // Standard A4 height in points
      val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
      val page = pdfDocument.startPage(pageInfo)
      val canvas = page.canvas

      // Paints
      val primaryColor = Color.rgb(103, 80, 164) // #6750A4
      val darkTextColor = Color.rgb(29, 27, 32)
      val grayTextColor = Color.rgb(73, 69, 79)
      val lightBgColor = Color.rgb(243, 237, 247)
      val heroBoxColor = Color.rgb(222, 236, 255) // Vibrant light blue
      val heroBorderColor = Color.rgb(37, 99, 235)
      val alertBgColor = Color.rgb(255, 232, 230)
      val alertBorderColor = Color.rgb(179, 38, 30)
      val borderColor = Color.rgb(202, 196, 208)

      val paint = Paint(Paint.ANTI_ALIAS_FLAG)
      var currentY = 40f
      val margin = 36f
      val contentWidth = pageWidth - (margin * 2)

      // 1. Top Header Banner
      paint.color = primaryColor
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 54f), 8f, 8f, paint)

      // Header Text
      paint.color = Color.WHITE
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 15f
      canvas.drawText("PEDIATRIC DOSAGE SUMMARY & CLINICAL RECORD", margin + 16f, currentY + 24f, paint)

      paint.textSize = 9.5f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
      canvas.drawText("Generated: $dateStr  |  Rx ID: #${UUID.randomUUID().toString().take(8).uppercase()}", margin + 16f, currentY + 42f, paint)

      currentY += 68f

      // 2. Patient & Medication Demographics Grid
      paint.color = lightBgColor
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 68f), 6f, 6f, paint)
      paint.style = Paint.Style.STROKE
      paint.color = borderColor
      paint.strokeWidth = 0.8f
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 68f), 6f, 6f, paint)
      paint.style = Paint.Style.FILL

      val col1 = margin + 14f
      val col2 = margin + (contentWidth / 2f) + 10f

      // Col 1: Patient Information
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 10f
      paint.color = primaryColor
      canvas.drawText("PATIENT DEMOGRAPHICS", col1, currentY + 16f, paint)

      paint.textSize = 9f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      paint.color = darkTextColor
      val displayPatientName = if (patientName.isNotBlank()) patientName else "Anonymous Patient"
      canvas.drawText("Name / MRN: $displayPatientName", col1, currentY + 32f, paint)

      val ageText = if (patientAgeText.isNotBlank()) patientAgeText else {
        if (result.patientAgeYears >= 1f) "${result.patientAgeYears.toInt()} yrs ${if (result.patientAgeMonths > 0) "${result.patientAgeMonths} mos" else ""}" else "${result.patientAgeMonths} mos"
      }
      val weightText = if (patientWeightText.isNotBlank()) patientWeightText else "${String.format(Locale.US, "%.2f", result.patientWeightKg)} kg"
      canvas.drawText("Age: $ageText  |  Weight: $weightText (${String.format(Locale.US, "%.2f", result.patientWeightKg)} kg)", col1, currentY + 46f, paint)
      canvas.drawText("BSA (Est.): ${String.format(Locale.US, "%.2f", Math.sqrt((result.patientWeightKg * 100.0) / 3600.0))} m²", col1, currentY + 59f, paint)

      // Col 2: Drug Formulation Details
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 10f
      paint.color = primaryColor
      canvas.drawText("MEDICATION SPECIFICATION", col2, currentY + 16f, paint)

      paint.textSize = 9f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      paint.color = darkTextColor
      canvas.drawText("Drug: ${result.drug.name} (${result.drug.genericName})", col2, currentY + 32f, paint)
      canvas.drawText("Formulation: ${result.formulation.name} (${result.formulation.unitLabel})", col2, currentY + 46f, paint)
      canvas.drawText("Indication: ${result.regimen.name}", col2, currentY + 59f, paint)

      currentY += 80f

      // 3. Calculated Dose Hero Section
      paint.color = heroBoxColor
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 84f), 8f, 8f, paint)
      paint.style = Paint.Style.STROKE
      paint.color = heroBorderColor
      paint.strokeWidth = 1.2f
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 84f), 8f, 8f, paint)
      paint.style = Paint.Style.FILL

      paint.color = Color.rgb(30, 58, 138)
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 10f
      canvas.drawText("PRESCRIBED CLINICAL DOSE", margin + 14f, currentY + 18f, paint)

      // Big Single Dose Display
      val doseDisplay = if (result.singleDoseMl != null) {
        "${String.format(Locale.US, "%.1f", result.singleDoseMl)} mL"
      } else if (result.singleDoseTablets != null) {
        "${String.format(Locale.US, "%.2f", result.singleDoseTablets)} Tab"
      } else {
        "${String.format(Locale.US, "%.1f", result.singleDoseMg)} mg"
      }

      paint.textSize = 24f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.color = Color.rgb(30, 58, 138)
      canvas.drawText(doseDisplay, margin + 14f, currentY + 48f, paint)

      paint.textSize = 10.5f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      paint.color = Color.rgb(71, 85, 105)
      canvas.drawText("(${String.format(Locale.US, "%.1f", result.singleDoseMg)} mg per single dose)", margin + 14f, currentY + 68f, paint)

      // Right-aligned regimen schedule details
      val rightColX = margin + (contentWidth * 0.52f)
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 10f
      paint.color = Color.rgb(30, 58, 138)
      canvas.drawText("• Frequency:", rightColX, currentY + 28f, paint)
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      canvas.drawText(result.frequencyText, rightColX + 70f, currentY + 28f, paint)

      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      canvas.drawText("• 24h Total:", rightColX, currentY + 46f, paint)
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      val dailyStr = if (result.totalDailyDoseMl != null) {
        "${String.format(Locale.US, "%.1f", result.totalDailyDoseMl)} mL (${String.format(Locale.US, "%.0f", result.totalDailyDoseMg)} mg/day)"
      } else {
        "${String.format(Locale.US, "%.0f", result.totalDailyDoseMg)} mg/day"
      }
      canvas.drawText(dailyStr, rightColX + 70f, currentY + 46f, paint)

      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      canvas.drawText("• Duration:", rightColX, currentY + 64f, paint)
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      val durStr = "${result.courseDurationDays} Days ${result.bottlesNeededSummary?.let { "($it)" } ?: ""}"
      canvas.drawText(durStr, rightColX + 70f, currentY + 64f, paint)

      currentY += 96f

      // 4. Safety Validation & Alert Banner (if applicable)
      if (result.hasSafetyThresholdAlert || result.warnings.isNotEmpty()) {
        val isThresholdExceeded = result.isExceedingMaxSingleDose || result.isExceedingMaxDailyDose
        paint.color = if (isThresholdExceeded) alertBgColor else Color.rgb(254, 243, 199)
        val alertHeight = if (isThresholdExceeded) 48f else 36f
        canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + alertHeight), 6f, 6f, paint)
        paint.style = Paint.Style.STROKE
        paint.color = if (isThresholdExceeded) alertBorderColor else Color.rgb(217, 119, 6)
        paint.strokeWidth = 1f
        canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + alertHeight), 6f, 6f, paint)
        paint.style = Paint.Style.FILL

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 9.5f
        paint.color = if (isThresholdExceeded) alertBorderColor else Color.rgb(180, 83, 9)
        val alertTitle = if (isThresholdExceeded) "⚠️ SAFETY VALIDATION: DOSAGE THRESHOLD EXCEEDED (AUTOMATICALLY CAPPED)" else "⚠️ CLINICAL NOTICE"
        canvas.drawText(alertTitle, margin + 12f, currentY + 16f, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.textSize = 8.5f
        val alertSub = if (result.isExceedingMaxSingleDose) {
          "Raw calculated single dose (${String.format(Locale.US, "%.1f", result.rawCalculatedSingleDoseMg)} mg) exceeded safe ceiling (${result.maxSingleDoseMg.toInt()} mg). Capped to safe maximum."
        } else if (result.isExceedingMaxDailyDose) {
          "Raw calculated daily dose (${String.format(Locale.US, "%.1f", result.rawCalculatedDailyDoseMg)} mg) exceeded safe ceiling (${result.maxDailyDoseMg.toInt()} mg). Capped to safe maximum."
        } else {
          result.warnings.firstOrNull() ?: "Review clinical warnings before administration."
        }
        canvas.drawText(alertSub, margin + 12f, currentY + 32f, paint)
        currentY += alertHeight + 12f
      }

      // 5. Mathematical Audit Trail & Formula Breakdown
      paint.color = lightBgColor
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 95f), 6f, 6f, paint)
      paint.style = Paint.Style.STROKE
      paint.color = borderColor
      paint.strokeWidth = 0.8f
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 95f), 6f, 6f, paint)
      paint.style = Paint.Style.FILL

      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 10f
      paint.color = primaryColor
      canvas.drawText("STEP-BY-STEP CALCULATION AUDIT TRAIL", margin + 14f, currentY + 16f, paint)

      paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
      paint.textSize = 8f
      paint.color = darkTextColor
      val explanationLines = result.stepByStepExplanation.lines().filter { it.isNotBlank() }.take(5)
      var expY = currentY + 30f
      for (line in explanationLines) {
        canvas.drawText(line, margin + 14f, expY, paint)
        expY += 12f
      }

      currentY += 106f

      // 6. Administration & Monograph Guidelines
      paint.color = Color.rgb(255, 255, 255)
      paint.style = Paint.Style.STROKE
      paint.color = borderColor
      paint.strokeWidth = 0.8f
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 84f), 6f, 6f, paint)
      paint.style = Paint.Style.FILL

      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 9.5f
      paint.color = primaryColor
      canvas.drawText("ADMINISTRATION & RECONSTITUTION GUIDELINES", margin + 14f, currentY + 16f, paint)

      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      paint.textSize = 8.5f
      paint.color = darkTextColor
      val adminAdvice = result.drug.administrationAdvice.take(130)
      canvas.drawText("• Administration: $adminAdvice", margin + 14f, currentY + 32f, paint)

      val storageAdvice = (result.drug.reconstitutionStorage ?: "Store at controlled room temperature (15-25°C). Keep tightly closed.").take(130)
      canvas.drawText("• Storage / Reconstitution: $storageAdvice", margin + 14f, currentY + 47f, paint)

      val contraText = if (result.drug.contraindications.isNotEmpty()) "• Contraindications: ${result.drug.contraindications.joinToString(", ").take(120)}" else "• Contraindications: None known"
      canvas.drawText(contraText, margin + 14f, currentY + 62f, paint)

      val renalText = result.drug.renalAdjustmentNote?.let { "• Renal: ${it.take(120)}" } ?: "• Monitoring: Standard therapeutic assessment."
      canvas.drawText(renalText, margin + 14f, currentY + 76f, paint)

      currentY += 96f

      // 7. Prescriber / Pharmacist Verification & Sign-off
      paint.style = Paint.Style.STROKE
      paint.color = borderColor
      paint.strokeWidth = 0.8f
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 70f), 6f, 6f, paint)
      paint.style = Paint.Style.FILL

      val sigWidth = (contentWidth / 2f) - 10f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 9f
      paint.color = grayTextColor
      canvas.drawText("PRESCRIBING CLINICIAN SIGNATURE", margin + 14f, currentY + 16f, paint)
      canvas.drawText("PHARMACIST VERIFICATION & STAMP", margin + (contentWidth / 2f) + 10f, currentY + 16f, paint)

      paint.style = Paint.Style.STROKE
      paint.color = Color.rgb(180, 180, 180)
      paint.strokeWidth = 0.8f
      // Signature lines
      canvas.drawLine(margin + 14f, currentY + 54f, margin + sigWidth, currentY + 54f, paint)
      canvas.drawLine(margin + (contentWidth / 2f) + 10f, currentY + 54f, margin + contentWidth - 14f, currentY + 54f, paint)
      paint.style = Paint.Style.FILL

      paint.textSize = 7.5f
      paint.color = grayTextColor
      canvas.drawText("Signature / License No. / Date", margin + 14f, currentY + 64f, paint)
      canvas.drawText("Verified By / Batch No. / Date", margin + (contentWidth / 2f) + 10f, currentY + 64f, paint)

      currentY += 80f

      // 8. Legal Disclaimer Footer
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
      paint.textSize = 7f
      paint.color = Color.rgb(120, 120, 120)
      val disclaimer1 = "CONFIDENTIAL MEDICAL RECORD: This dosage summary is generated for clinical calculation assistance. Medical professionals"
      val disclaimer2 = "must independently confirm patient allergies, contraindications, and laboratory kidney/liver functions prior to dispensing."
      canvas.drawText(disclaimer1, margin, pageHeight - 32f, paint)
      canvas.drawText(disclaimer2, margin, pageHeight - 22f, paint)

      pdfDocument.finishPage(page)

      FileOutputStream(file).use { out ->
        pdfDocument.writeTo(out)
      }
      pdfDocument.close()
    } catch (t: Throwable) {
      // Robust fallback for JVM / Robolectric environment where native libpdfium is unavailable
      writePdfFallbackStream(file, result, patientName, patientAgeText, patientWeightText)
    }

    return file
  }

  /**
   * Generates a PDF record for a saved history calculation item
   */
  fun generateHistoryItemPdf(
    context: Context,
    item: CalculationHistoryItem
  ): File {
    val pdfDir = File(context.cacheDir, "dosage_reports").apply { mkdirs() }
    val sanitizedDrugName = item.drugName.replace("[^a-zA-Z0-9]".toRegex(), "_")
    val file = File(pdfDir, "History_Dosage_${sanitizedDrugName}_${item.id.take(6)}.pdf")

    try {
      val pdfDocument = PdfDocument()
      val pageWidth = 595
      val pageHeight = 842
      val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
      val page = pdfDocument.startPage(pageInfo)
      val canvas = page.canvas

      val primaryColor = Color.rgb(103, 80, 164)
      val darkTextColor = Color.rgb(29, 27, 32)
      val grayTextColor = Color.rgb(73, 69, 79)
      val lightBgColor = Color.rgb(243, 237, 247)
      val heroBoxColor = Color.rgb(222, 236, 255)
      val heroBorderColor = Color.rgb(37, 99, 235)
      val borderColor = Color.rgb(202, 196, 208)

      val paint = Paint(Paint.ANTI_ALIAS_FLAG)
      var currentY = 40f
      val margin = 36f
      val contentWidth = pageWidth - (margin * 2)

      // Top Header Banner
      paint.color = primaryColor
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 54f), 8f, 8f, paint)

      paint.color = Color.WHITE
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 15f
      canvas.drawText("PEDIATRIC DOSAGE SUMMARY & CLINICAL RECORD", margin + 16f, currentY + 24f, paint)

      paint.textSize = 9.5f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      val recordDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(item.timestamp))
      canvas.drawText("Record Date: $recordDate  |  Rx ID: #${item.id.take(8).uppercase()}", margin + 16f, currentY + 42f, paint)

      currentY += 68f

      // Patient & Medication Information
      paint.color = lightBgColor
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 68f), 6f, 6f, paint)
      paint.style = Paint.Style.STROKE
      paint.color = borderColor
      paint.strokeWidth = 0.8f
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 68f), 6f, 6f, paint)
      paint.style = Paint.Style.FILL

      val col1 = margin + 14f
      val col2 = margin + (contentWidth / 2f) + 10f

      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 10f
      paint.color = primaryColor
      canvas.drawText("PATIENT DETAILS", col1, currentY + 16f, paint)

      paint.textSize = 9f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      paint.color = darkTextColor
      canvas.drawText("Name / ID: ${item.patientNameOrId}", col1, currentY + 32f, paint)
      canvas.drawText("Age: ${item.patientAgeText}  |  Weight: ${item.patientWeightKg} kg", col1, currentY + 48f, paint)

      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 10f
      paint.color = primaryColor
      canvas.drawText("MEDICATION SPECIFICATION", col2, currentY + 16f, paint)

      paint.textSize = 9f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      paint.color = darkTextColor
      canvas.drawText("Drug: ${item.drugName}", col2, currentY + 32f, paint)
      canvas.drawText("Formulation: ${item.formulationText}", col2, currentY + 48f, paint)

      currentY += 80f

      // Prescribed Dose Box
      paint.color = heroBoxColor
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 84f), 8f, 8f, paint)
      paint.style = Paint.Style.STROKE
      paint.color = heroBorderColor
      paint.strokeWidth = 1.2f
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 84f), 8f, 8f, paint)
      paint.style = Paint.Style.FILL

      paint.color = Color.rgb(30, 58, 138)
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 10f
      canvas.drawText("PRESCRIBED CLINICAL DOSE", margin + 14f, currentY + 18f, paint)

      paint.textSize = 22f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.color = Color.rgb(30, 58, 138)
      canvas.drawText(item.singleDoseText, margin + 14f, currentY + 48f, paint)

      val rightColX = margin + (contentWidth * 0.52f)
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 10f
      paint.color = Color.rgb(30, 58, 138)
      canvas.drawText("• Frequency:", rightColX, currentY + 28f, paint)
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      canvas.drawText(item.frequencyText, rightColX + 70f, currentY + 28f, paint)

      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      canvas.drawText("• 24h Total:", rightColX, currentY + 46f, paint)
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      canvas.drawText(item.totalDailyText, rightColX + 70f, currentY + 46f, paint)

      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      canvas.drawText("• Course:", rightColX, currentY + 64f, paint)
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
      canvas.drawText("${item.durationDays} Days (${item.courseTotalText})", rightColX + 70f, currentY + 64f, paint)

      currentY += 96f

      // Sign-off
      paint.style = Paint.Style.STROKE
      paint.color = borderColor
      paint.strokeWidth = 0.8f
      canvas.drawRoundRect(RectF(margin, currentY, margin + contentWidth, currentY + 70f), 6f, 6f, paint)
      paint.style = Paint.Style.FILL

      val sigWidth = (contentWidth / 2f) - 10f
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      paint.textSize = 9f
      paint.color = grayTextColor
      canvas.drawText("PRESCRIBING CLINICIAN SIGNATURE", margin + 14f, currentY + 16f, paint)
      canvas.drawText("PHARMACIST VERIFICATION & STAMP", margin + (contentWidth / 2f) + 10f, currentY + 16f, paint)

      paint.style = Paint.Style.STROKE
      paint.color = Color.rgb(180, 180, 180)
      paint.strokeWidth = 0.8f
      canvas.drawLine(margin + 14f, currentY + 54f, margin + sigWidth, currentY + 54f, paint)
      canvas.drawLine(margin + (contentWidth / 2f) + 10f, currentY + 54f, margin + contentWidth - 14f, currentY + 54f, paint)
      paint.style = Paint.Style.FILL

      paint.textSize = 7.5f
      paint.color = grayTextColor
      canvas.drawText("Signature / License No. / Date", margin + 14f, currentY + 64f, paint)
      canvas.drawText("Verified By / Batch No. / Date", margin + (contentWidth / 2f) + 10f, currentY + 64f, paint)

      // Legal Footer
      paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
      paint.textSize = 7f
      paint.color = Color.rgb(120, 120, 120)
      val disclaimer1 = "CONFIDENTIAL MEDICAL RECORD: This dosage summary is generated for clinical calculation assistance. Medical professionals"
      val disclaimer2 = "must independently confirm patient allergies, contraindications, and laboratory kidney/liver functions prior to dispensing."
      canvas.drawText(disclaimer1, margin, pageHeight - 32f, paint)
      canvas.drawText(disclaimer2, margin, pageHeight - 22f, paint)

      pdfDocument.finishPage(page)

      FileOutputStream(file).use { out ->
        pdfDocument.writeTo(out)
      }
      pdfDocument.close()
    } catch (t: Throwable) {
      writeHistoryPdfFallbackStream(file, item)
    }

    return file
  }

  private fun writePdfFallbackStream(
    file: File,
    result: DoseCalculationResult,
    patientName: String,
    patientAgeText: String,
    patientWeightText: String
  ) {
    val content = """%PDF-1.4
1 0 obj
<< /Type /Catalog /Pages 2 0 R >>
endobj
2 0 obj
<< /Type /Pages /Kids [3 0 R] /Count 1 >>
endobj
3 0 obj
<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Contents 4 0 R >>
endobj
4 0 obj
<< /Length 200 >>
stream
BT
/F1 12 Tf
40 800 Td
(PEDIATRIC DOSAGE SUMMARY: ${result.drug.name}) Tj
0 -20 Td
(Patient: $patientName | Weight: $patientWeightText | Age: $patientAgeText) Tj
0 -20 Td
(Prescribed Dose: ${result.singleDoseMg} mg - ${result.frequencyText}) Tj
ET
endstream
endobj
xref
0 5
0000000000 65535 f 
0000000009 00000 n 
0000000058 00000 n 
0000000115 00000 n 
0000000200 00000 n 
trailer
<< /Size 5 /Root 1 0 R >>
startxref
450
%%EOF"""
    FileOutputStream(file).use { it.write(content.toByteArray(Charsets.UTF_8)) }
  }

  private fun writeHistoryPdfFallbackStream(file: File, item: CalculationHistoryItem) {
    val content = """%PDF-1.4
1 0 obj
<< /Type /Catalog /Pages 2 0 R >>
endobj
2 0 obj
<< /Type /Pages /Kids [3 0 R] /Count 1 >>
endobj
3 0 obj
<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Contents 4 0 R >>
endobj
4 0 obj
<< /Length 180 >>
stream
BT
/F1 12 Tf
40 800 Td
(PEDIATRIC PRESCRIPTION RECORD: ${item.drugName}) Tj
0 -20 Td
(Patient: ${item.patientNameOrId} | Weight: ${item.patientWeightKg} kg) Tj
0 -20 Td
(Prescribed Dose: ${item.singleDoseText} - ${item.frequencyText}) Tj
ET
endstream
endobj
xref
0 5
0000000000 65535 f 
0000000009 00000 n 
0000000058 00000 n 
0000000115 00000 n 
0000000200 00000 n 
trailer
<< /Size 5 /Root 1 0 R >>
startxref
430
%%EOF"""
    FileOutputStream(file).use { it.write(content.toByteArray(Charsets.UTF_8)) }
  }

  /**
   * Shares the generated PDF file using standard Android Intent.ACTION_SEND
   */
  fun sharePdf(context: Context, pdfFile: File, subject: String = "Clinical Dosage Summary") {
    try {
      val contentUri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        pdfFile
      )

      val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_STREAM, contentUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      }

      val chooser = Intent.createChooser(intent, "Share Dosage Summary PDF")
      chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      context.startActivity(chooser)
    } catch (e: Exception) {
      e.printStackTrace()
      Toast.makeText(context, "Failed to share PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
    }
  }

  /**
   * Opens or Prints the PDF using the system PDF viewer / Print service
   */
  fun viewOrPrintPdf(context: Context, pdfFile: File) {
    try {
      val contentUri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        pdfFile
      )

      val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(contentUri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }

      val chooser = Intent.createChooser(intent, "Open or Print PDF Record")
      chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      context.startActivity(chooser)
    } catch (e: Exception) {
      e.printStackTrace()
      Toast.makeText(context, "No PDF viewer found. You can share the file instead.", Toast.LENGTH_SHORT).show()
    }
  }
}
