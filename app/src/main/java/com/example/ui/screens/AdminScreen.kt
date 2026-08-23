import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Apne DrugCsvImporter ka import yahan zaroor check karein
// import com.example.drugdoseinfo.util.DrugCsvImporter

@Composable
fun FormularyAdministrationCard(
    viewModel: DrugViewModel, // Apna ViewModel yahan pass karein
    totalDrugs: Int = 12,     // Database se live data yahan aayega
    customEntries: Int = 0,
    strengths: Int = 48
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9FC)), // Light grayish-white background
        border = BorderStroke(1.dp, Color(0xFFD1D1D6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // --- HEADER ROW ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Formulary Administration",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1C1B1F)
                )

                Button(
                    onClick = { /* Add Drug Logic */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Drug", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- STATS CARDS ROW ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Total Drugs (Purple)
                StatBox(
                    modifier = Modifier.weight(1f),
                    count = totalDrugs.toString(),
                    label = "Total Drugs",
                    backgroundColor = Color(0xFFE8DEF8),
                    textColor = Color(0xFF381E72)
                )

                // Custom Entries (Green)
                StatBox(
                    modifier = Modifier.weight(1f),
                    count = customEntries.toString(),
                    label = "Custom Entries",
                    backgroundColor = Color(0xFFD7F8D3),
                    textColor = Color(0xFF0F5223)
                )

                // Strengths (Pink)
                StatBox(
                    modifier = Modifier.weight(1f),
                    count = strengths.toString(),
                    label = "Strengths",
                    backgroundColor = Color(0xFFFFD8E4),
                    textColor = Color(0xFF492532)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- ACTION BUTTONS (SHARE & RESTORE) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Share Logic */ },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("Share Formulary", color = Color(0xFFC8A2C8)) // Light purple text
                }

                OutlinedButton(
                    onClick = { /* Restore Logic */ },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color(0xFFD6777A))
                ) {
                    Text("Restore Defaults", color = Color(0xFFA13538), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- NEW: IMPORT CSV BUTTON ---
            Button(
                onClick = {
                    try {
                        // Asset folder se CSV read karein
                        val parsedDrugs = DrugCsvImporter.readCsvFromAssets(context, "DrugsTemplate.csv")
                        
                        // Database mein insert karein (Apne viewModel ka exact function call karein)
                        viewModel.insertAllDrugs(parsedDrugs) 
                        
                        Toast.makeText(context, "Success! ${parsedDrugs.size} Drugs Imported.", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)), // Match Add Drug button color
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Import Data from CSV",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

// --- HELPER COMPOSABLE FOR STAT BOXES ---
@Composable
fun StatBox(modifier: Modifier = Modifier, count: String, label: String, backgroundColor: Color, textColor: Color) {
    Column(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}