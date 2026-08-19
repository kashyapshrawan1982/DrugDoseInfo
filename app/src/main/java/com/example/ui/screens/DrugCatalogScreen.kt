package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Drug
import com.example.model.DrugCategory
import com.example.ui.theme.VibrantOutlineVariant
import com.example.ui.theme.VibrantPrimary
import com.example.ui.theme.VibrantPrimaryContainer
import com.example.ui.theme.VibrantSurface
import com.example.viewmodel.DoseCalculatorUiState
import com.example.viewmodel.DoseCalculatorViewModel

@Composable
fun DrugCatalogScreen(
  state: DoseCalculatorUiState,
  viewModel: DoseCalculatorViewModel,
  onSelectDrugAndCalculate: (Drug) -> Unit,
  onViewDrugDetails: (Drug) -> Unit,
  modifier: Modifier = Modifier
) {
  val filteredDrugs = remember(state.drugList, state.searchQuery, state.selectedCategory) {
    state.drugList.filter { drug ->
      val matchesCategory = state.selectedCategory == DrugCategory.ALL || drug.category == state.selectedCategory
      val matchesQuery = state.searchQuery.isBlank() ||
        drug.name.contains(state.searchQuery, ignoreCase = true) ||
        drug.genericName.contains(state.searchQuery, ignoreCase = true) ||
        drug.subtitle.contains(state.searchQuery, ignoreCase = true) ||
        drug.indications.any { it.contains(state.searchQuery, ignoreCase = true) }
      matchesCategory && matchesQuery
    }
  }

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
          imageVector = Icons.Default.Medication,
          contentDescription = null,
          tint = VibrantPrimary,
          modifier = Modifier.size(22.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = "Formulary Catalog",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1D1B20)
        )
        Text(
          text = "${filteredDrugs.size} verified medications",
          fontSize = 12.sp,
          color = Color(0xFF49454F)
        )
      }
    }

    // Search Box
    OutlinedTextField(
      value = state.searchQuery,
      onValueChange = { viewModel.setSearchQuery(it) },
      placeholder = { Text("Search drug by name, generic, or indication...", fontSize = 13.sp) },
      leadingIcon = {
        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = VibrantPrimary)
      },
      trailingIcon = {
        if (state.searchQuery.isNotBlank()) {
          IconButton(onClick = { viewModel.setSearchQuery("") }) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search", tint = Color(0xFF49454F))
          }
        }
      },
      shape = RoundedCornerShape(16.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = VibrantSurface,
        unfocusedContainerColor = VibrantSurface,
        focusedBorderColor = VibrantPrimary,
        unfocusedBorderColor = VibrantOutlineVariant
      ),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)
        .testTag("drug_search_bar"),
      singleLine = true
    )

    // Category Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      DrugCategory.values().forEach { cat ->
        val isSelected = state.selectedCategory == cat
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) VibrantPrimary else Color(0xFFE8DEF8))
            .clickable { viewModel.setCategoryFilter(cat) }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("category_chip_${cat.name}")
        ) {
          Text(
            text = cat.displayName,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF1D1B20)
          )
        }
      }
    }

    // Drug List
    if (filteredDrugs.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Medication,
            contentDescription = null,
            tint = Color(0xFF79747E),
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No medications found matching \"${state.searchQuery}\"",
            color = Color(0xFF49454F),
            fontSize = 14.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Button(
            onClick = { viewModel.startAddDrug() },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary)
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add This Drug")
          }
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(filteredDrugs, key = { it.id }) { drug ->
          DrugCatalogCard(
            drug = drug,
            onCalculate = { onSelectDrugAndCalculate(drug) },
            onDetails = { onViewDrugDetails(drug) }
          )
        }
        item {
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}

@Composable
private fun DrugCatalogCard(
  drug: Drug,
  onCalculate: () -> Unit,
  onDetails: () -> Unit
) {
  Surface(
    color = VibrantSurface,
    shape = RoundedCornerShape(16.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutlineVariant),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("drug_card_${drug.id}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = drug.name,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = VibrantPrimary
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
            text = drug.genericName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF49454F)
          )
        }
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFEADDFF))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = drug.category.displayName,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF21005D)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = drug.description,
        fontSize = 12.sp,
        color = Color(0xFF49454F),
        lineHeight = 16.sp,
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(8.dp))
      // Available Concentrations preview
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        drug.formulations.forEach { form ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFF3EDF7))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = form.unitLabel,
              fontSize = 10.sp,
              color = Color(0xFF625B71),
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      // Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = onCalculate,
          shape = RoundedCornerShape(50),
          colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary),
          modifier = Modifier
            .weight(1.3f)
            .height(40.dp)
            .testTag("btn_calculate_${drug.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Calculate,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Calculate Dose", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
          onClick = onDetails,
          shape = RoundedCornerShape(50),
          modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .testTag("btn_details_${drug.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = VibrantPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "Details", fontSize = 12.sp, color = VibrantPrimary, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
