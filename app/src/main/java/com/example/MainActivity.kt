package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.components.AppBottomNavigationBar
import com.example.ui.screens.AddEditDrugScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.DrugCatalogScreen
import com.example.ui.screens.DrugDetailScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppTab
import com.example.viewmodel.DoseCalculatorViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: DoseCalculatorViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val state by viewModel.uiState.collectAsState()
      val snackbarHostState = remember { SnackbarHostState() }

      LaunchedEffect(state.snackbarMessage) {
        state.snackbarMessage?.let { message ->
          snackbarHostState.showSnackbar(message)
          viewModel.clearSnackbar()
        }
      }

      MyApplicationTheme(
        colorTheme = state.activeColorTheme,
        darkModePreference = state.darkModePreference
      ) {
        if (state.isAddingOrEditingDrug && state.isAdminAuthenticated) {
          AddEditDrugScreen(
            existingDrug = state.drugBeingEdited,
            onSave = { updatedOrNewDrug ->
              viewModel.saveDrug(updatedOrNewDrug)
            },
            onCancel = {
              viewModel.closeAddEditDrug()
            }
          )
        } else {
          Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
              AppBottomNavigationBar(
                currentTab = state.currentTab,
                onTabSelected = { tab -> viewModel.setTab(tab) }
              )
            }
          ) { innerPadding ->
            when (state.currentTab) {
              AppTab.CALCULATOR -> {
                CalculatorScreen(
                  state = state,
                  viewModel = viewModel,
                  onOpenDrugList = { viewModel.setTab(AppTab.DRUGS) },
                  onViewDrugDetail = { drug -> viewModel.viewDrugDetails(drug) },
                  modifier = Modifier.padding(innerPadding)
                )
              }
              AppTab.DRUGS -> {
                DrugCatalogScreen(
                  state = state,
                  viewModel = viewModel,
                  onSelectDrugAndCalculate = { drug -> viewModel.selectDrugAndOpenCalculator(drug) },
                  onViewDrugDetails = { drug -> viewModel.viewDrugDetails(drug) },
                  modifier = Modifier.padding(innerPadding)
                )
              }
              AppTab.DRUG_DETAILS -> {
                DrugDetailScreen(
                  drug = state.detailDrug,
                  onBack = { viewModel.setTab(AppTab.DRUGS) },
                  onCalculateDose = { drug -> viewModel.selectDrugAndOpenCalculator(drug) },
                  modifier = Modifier.padding(innerPadding)
                )
              }
              AppTab.HISTORY -> {
                HistoryScreen(
                  state = state,
                  viewModel = viewModel,
                  modifier = Modifier.padding(innerPadding)
                )
              }
              AppTab.TOOLS -> {
                ToolsScreen(
                  modifier = Modifier.padding(innerPadding)
                )
              }
              AppTab.ADMIN -> {
                AdminScreen(
                  state = state,
                  onAuthenticate = { credential -> viewModel.authenticateAdmin(credential) },
                  onLogout = { viewModel.logoutAdmin() },
                  onChangePin = { oldPin, newPin -> viewModel.changeAdminPin(oldPin, newPin) },
                  onChangePassword = { oldPassword, newPassword -> viewModel.changeAdminPassword(oldPassword, newPassword) },
                  onResetCredentials = { viewModel.resetAdminCredentialsToDefaults() },
                  onSetLockMode = { mode -> viewModel.setAdminLockMode(mode) },
                  onAddNewDrug = { viewModel.startAddDrug() },
                  onEditDrug = { drug -> viewModel.startEditDrug(drug) },
                  onDeleteDrug = { drugId -> viewModel.deleteDrug(drugId) },
                  onResetDefaults = { viewModel.resetFormularyToDefaults() },
                  onSelectColorTheme = { theme -> viewModel.setColorTheme(theme) },
                  onSelectDarkMode = { mode -> viewModel.setDarkModePreference(mode) },
                  modifier = Modifier.padding(innerPadding)
                )
              }
            }
          }
        }
      }
    }
  }
}
