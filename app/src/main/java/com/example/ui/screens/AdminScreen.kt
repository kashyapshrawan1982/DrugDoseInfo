package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Drug
import com.example.model.DrugCategory
import com.example.ui.theme.AppColorTheme
import com.example.ui.theme.DarkModePreference
import com.example.ui.theme.VibrantOutlineVariant
import com.example.ui.theme.VibrantPrimary
import com.example.ui.theme.VibrantPrimaryContainer
import com.example.ui.theme.VibrantSurface
import com.example.util.AdminCredentialsManager
import com.example.util.AdminLockMode
import com.example.util.PasswordStrength
import com.example.viewmodel.DoseCalculatorUiState

@Composable
fun AdminScreen(
  state: DoseCalculatorUiState,
  onAuthenticate: (String) -> Boolean,
  onLogout: () -> Unit,
  onChangePin: (String, String) -> Boolean,
  onChangePassword: (String, String) -> Boolean,
  onResetCredentials: () -> Unit = {},
  onSetLockMode: (AdminLockMode) -> Unit = {},
  onAddNewDrug: () -> Unit,
  onEditDrug: (Drug) -> Unit,
  onDeleteDrug: (String) -> Unit,
  onResetDefaults: () -> Unit,
  onSelectColorTheme: (AppColorTheme) -> Unit,
  onSelectDarkMode: (DarkModePreference) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var adminSearchQuery by remember { mutableStateOf("") }
  var drugToDelete by remember { mutableStateOf<Drug?>(null) }
  var showResetConfirmDialog by remember { mutableStateOf(false) }
  var showChangePinDialog by remember { mutableStateOf(false) }
  var showChangePasswordDialog by remember { mutableStateOf(false) }
  var showResetCredentialsDialog by remember { mutableStateOf(false) }

  // IF ADMIN IS LOCKED / NOT AUTHENTICATED -> SHOW LOCK SCREEN
  if (!state.isAdminAuthenticated) {
    AdminPasscodeLockView(
      initialLockMode = state.adminLockMode,
      onUnlock = { credential -> onAuthenticate(credential) },
      modifier = modifier
    )
    return
  }

  // --- AUTHENTICATED ADMIN DASHBOARD ---
  val filteredDrugs = remember(state.drugList, adminSearchQuery) {
    state.drugList.filter { drug ->
      adminSearchQuery.isBlank() ||
        drug.name.contains(adminSearchQuery, ignoreCase = true) ||
        drug.genericName.contains(adminSearchQuery, ignoreCase = true) ||
        drug.category.displayName.contains(adminSearchQuery, ignoreCase = true)
    }
  }

  val customDrugsCount = remember(state.drugList) { state.drugList.count { it.isCustom } }
  val totalFormulationsCount = remember(state.drugList) { state.drugList.sumOf { it.formulations.size } }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F2FA))
  ) {
    // Header with Security Status & Action Icons
    Surface(
      color = VibrantSurface,
      tonalElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(Color(0xFFD4F7DC)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = Color(0xFF0F5132),
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Admin Portal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(Color(0xFFD4F7DC))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "AUTHENTICATED",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF0F5132)
                )
              }
            }
            Text(
              text = "Security, Formulary & Settings",
              fontSize = 11.sp,
              color = Color(0xFF49454F)
            )
          }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          IconButton(
            onClick = { showChangePinDialog = true },
            modifier = Modifier.testTag("btn_admin_change_pin")
          ) {
            Icon(
              imageVector = Icons.Default.Key,
              contentDescription = "Change PIN",
              tint = VibrantPrimary
            )
          }

          IconButton(
            onClick = { showChangePasswordDialog = true },
            modifier = Modifier.testTag("btn_admin_change_password")
          ) {
            Icon(
              imageVector = Icons.Default.Password,
              contentDescription = "Change Password",
              tint = VibrantPrimary
            )
          }

          IconButton(
            onClick = onLogout,
            modifier = Modifier.testTag("btn_admin_lock_session")
          ) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = "Lock Admin",
              tint = Color(0xFFB3261E)
            )
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. ADMIN SECURITY & CREDENTIALS MANAGEMENT CARD
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = VibrantSurface),
          shape = RoundedCornerShape(20.dp),
          border = BorderStroke(1.dp, VibrantOutlineVariant),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEADDFF)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = Color(0xFF21005D),
                    modifier = Modifier.size(18.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "Security & Access Credentials",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D1B20)
                  )
                  Text(
                    text = "Manage Admin PIN & Master Password",
                    fontSize = 11.sp,
                    color = Color(0xFF49454F)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Credentials Detail Rows
            // Row A: Admin PIN
            Surface(
              color = Color(0xFFF7F2FA),
              shape = RoundedCornerShape(12.dp),
              border = BorderStroke(0.8.dp, Color(0xFFE7E0EC)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Pin,
                    contentDescription = null,
                    tint = VibrantPrimary,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = "Admin PIN",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D1B20)
                      )
                      Spacer(modifier = Modifier.width(6.dp))
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(4.dp))
                          .background(Color(0xFFE8DEF8))
                          .padding(horizontal = 6.dp, vertical = 1.dp)
                      ) {
                        Text(
                          text = "${state.adminPin.length} digits",
                          fontSize = 9.5.sp,
                          fontWeight = FontWeight.SemiBold,
                          color = Color(0xFF21005D)
                        )
                      }
                    }
                    Text(
                      text = "Used for quick keypad unlock (● ● ● ●)",
                      fontSize = 11.sp,
                      color = Color(0xFF49454F)
                    )
                  }
                }

                Button(
                  onClick = { showChangePinDialog = true },
                  shape = RoundedCornerShape(50),
                  colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary),
                  modifier = Modifier
                    .height(34.dp)
                    .testTag("btn_change_pin_card")
                ) {
                  Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Change PIN", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row B: Admin Master Password
            val passwordStrength = remember(state.adminPassword) {
              AdminCredentialsManager.assessPasswordStrength(state.adminPassword)
            }

            Surface(
              color = Color(0xFFF7F2FA),
              shape = RoundedCornerShape(12.dp),
              border = BorderStroke(0.8.dp, Color(0xFFE7E0EC)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.VpnKey,
                    contentDescription = null,
                    tint = Color(0xFF6750A4),
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = "Master Password",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D1B20)
                      )
                      Spacer(modifier = Modifier.width(6.dp))
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(4.dp))
                          .background(
                            when (passwordStrength) {
                              PasswordStrength.STRONG -> Color(0xFFD4F7DC)
                              PasswordStrength.MODERATE -> Color(0xFFFFF3CD)
                              PasswordStrength.WEAK -> Color(0xFFFFD8E4)
                            }
                          )
                          .padding(horizontal = 6.dp, vertical = 1.dp)
                      ) {
                        Text(
                          text = passwordStrength.label,
                          fontSize = 9.5.sp,
                          fontWeight = FontWeight.Bold,
                          color = when (passwordStrength) {
                            PasswordStrength.STRONG -> Color(0xFF0F5132)
                            PasswordStrength.MODERATE -> Color(0xFF664D03)
                            PasswordStrength.WEAK -> Color(0xFFB3261E)
                          }
                        )
                      }
                    }
                    Text(
                      text = "Alphanumeric password for admin recovery",
                      fontSize = 11.sp,
                      color = Color(0xFF49454F)
                    )
                  }
                }

                Button(
                  onClick = { showChangePasswordDialog = true },
                  shape = RoundedCornerShape(50),
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                  modifier = Modifier
                    .height(34.dp)
                    .testTag("btn_change_password_card")
                ) {
                  Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Change Password", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Preferred Lock Screen Mode Selection
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Default Lock Mode:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D1B20)
              )

              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(
                  selected = state.adminLockMode == AdminLockMode.PIN,
                  onClick = { onSetLockMode(AdminLockMode.PIN) },
                  label = { Text("Numeric PIN", fontSize = 11.sp) },
                  leadingIcon = {
                    Icon(imageVector = Icons.Default.Pin, contentDescription = null, modifier = Modifier.size(14.dp))
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = VibrantPrimaryContainer,
                    selectedLabelColor = Color(0xFF21005D)
                  )
                )

                FilterChip(
                  selected = state.adminLockMode == AdminLockMode.PASSWORD,
                  onClick = { onSetLockMode(AdminLockMode.PASSWORD) },
                  label = { Text("Password", fontSize = 11.sp) },
                  leadingIcon = {
                    Icon(imageVector = Icons.Default.Password, contentDescription = null, modifier = Modifier.size(14.dp))
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = VibrantPrimaryContainer,
                    selectedLabelColor = Color(0xFF21005D)
                  )
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Reset credentials button
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              TextButton(
                onClick = { showResetCredentialsDialog = true },
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFB3261E))
              ) {
                Icon(imageVector = Icons.Default.LockReset, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset PIN & Password to Defaults", fontSize = 11.sp)
              }
            }
          }
        }
      }

      // 2. UI COLOR THEME SELECTOR CARD
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = VibrantSurface),
          shape = RoundedCornerShape(20.dp),
          border = BorderStroke(1.dp, VibrantOutlineVariant),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = null,
                tint = VibrantPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "UI Theme & Accent Color",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20)
              )
            }
            Text(
              text = "Select your preferred clinical palette. Updates the entire application immediately.",
              fontSize = 12.sp,
              color = Color(0xFF49454F),
              modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            // Color Swatches
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              AppColorTheme.values().forEach { theme ->
                val isSelected = state.activeColorTheme == theme
                Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier
                    .clickable { onSelectColorTheme(theme) }
                    .padding(4.dp)
                    .testTag("theme_color_${theme.name.lowercase()}")
                ) {
                  Box(
                    modifier = Modifier
                      .size(46.dp)
                      .clip(CircleShape)
                      .background(theme.primaryColor)
                      .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) Color(0xFF1D1B20) else Color.Transparent,
                        shape = CircleShape
                      ),
                    contentAlignment = Alignment.Center
                  ) {
                    if (isSelected) {
                      Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                      )
                    }
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = theme.displayName.split(" ").first(),
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) VibrantPrimary else Color(0xFF49454F)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dark Mode Preferences
            Text(
              text = "Display Mode",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF1D1B20)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              DarkModePreference.values().forEach { pref ->
                val isSelected = state.darkModePreference == pref
                FilterChip(
                  selected = isSelected,
                  onClick = { onSelectDarkMode(pref) },
                  label = {
                    Text(
                      text = pref.displayName,
                      fontSize = 11.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                  },
                  leadingIcon = {
                    Icon(
                      imageVector = when (pref) {
                        DarkModePreference.SYSTEM -> Icons.Default.ColorLens
                        DarkModePreference.LIGHT -> Icons.Default.LightMode
                        DarkModePreference.DARK -> Icons.Default.DarkMode
                      },
                      contentDescription = null,
                      modifier = Modifier.size(16.dp)
                    )
                  },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = VibrantPrimaryContainer,
                    selectedLabelColor = Color(0xFF21005D),
                    selectedLeadingIconColor = Color(0xFF21005D)
                  ),
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }
        }
      }

      // 3. STATS & FORMULARY ACTIONS CARD
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = VibrantSurface),
          shape = RoundedCornerShape(20.dp),
          border = BorderStroke(1.dp, VibrantOutlineVariant),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Formulary Administration",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20)
              )

              Button(
                onClick = onAddNewDrug,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary),
                modifier = Modifier
                  .height(38.dp)
                  .testTag("btn_admin_add_drug")
              ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Drug", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Metrics row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(0xFFEADDFF))
                  .padding(10.dp),
                contentAlignment = Alignment.Center
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(text = "${state.drugList.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF21005D))
                  Text(text = "Total Drugs", fontSize = 11.sp, color = Color(0xFF381E72))
                }
              }

              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(0xFFD4F7DC))
                  .padding(10.dp),
                contentAlignment = Alignment.Center
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(text = "$customDrugsCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F5132))
                  Text(text = "Custom Entries", fontSize = 11.sp, color = Color(0xFF0F5132))
                }
              }

              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(0xFFFFD8E4))
                  .padding(10.dp),
                contentAlignment = Alignment.Center
              ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(text = "$totalFormulationsCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF31111D))
                  Text(text = "Strengths", fontSize = 11.sp, color = Color(0xFF31111D))
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick utility buttons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              OutlinedButton(
                onClick = {
                  val summary = buildString {
                    appendLine("📋 CLINICAL FORMULARY EXPORT")
                    appendLine("Total Medications: ${state.drugList.size}")
                    appendLine("--------------------------------")
                    state.drugList.forEachIndexed { i, d ->
                      appendLine("${i + 1}. ${d.name} (${d.genericName}) [${d.category.displayName}]")
                      appendLine("   • Formulations: ${d.formulations.joinToString { it.name }}")
                      appendLine("   • Standard Regimen: ${d.standardRegimenSummary}")
                    }
                  }
                  val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Clinical Formulary List")
                    putExtra(Intent.EXTRA_TEXT, summary)
                  }
                  context.startActivity(Intent.createChooser(sendIntent, "Share Formulary"))
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier.weight(1f)
              ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Share Formulary", fontSize = 11.sp)
              }

              OutlinedButton(
                onClick = { showResetConfirmDialog = true },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB3261E)),
                border = BorderStroke(1.dp, Color(0xFFB3261E).copy(alpha = 0.5f)),
                modifier = Modifier.weight(1f)
              ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Restore Defaults", fontSize = 11.sp)
              }
            }
          }
        }
      }

      // 4. SEARCH & DRUG MANAGEMENT LIST HEADER
      item {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Formulary Medications (${filteredDrugs.size})",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1D1B20)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = adminSearchQuery,
            onValueChange = { adminSearchQuery = it },
            placeholder = { Text("Search drugs to edit or delete...", fontSize = 13.sp) },
            leadingIcon = {
              Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = VibrantPrimary)
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
        }
      }

      // 5. LIST OF DRUGS WITH EDIT & DELETE ACTIONS
      items(filteredDrugs, key = { it.id }) { drug ->
        Surface(
          color = VibrantSurface,
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, VibrantOutlineVariant),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = drug.name,
                  fontSize = 15.sp,
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
                text = "${drug.genericName} • ${drug.category.displayName}",
                fontSize = 12.sp,
                color = Color(0xFF49454F)
              )

              Text(
                text = "${drug.formulations.size} formulations • ${drug.defaultRegimen.name}",
                fontSize = 11.sp,
                color = VibrantPrimary
              )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              IconButton(
                onClick = { onEditDrug(drug) },
                modifier = Modifier
                  .size(36.dp)
                  .testTag("btn_edit_drug_${drug.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Edit,
                  contentDescription = "Edit Drug",
                  tint = VibrantPrimary,
                  modifier = Modifier.size(18.dp)
                )
              }

              IconButton(
                onClick = { drugToDelete = drug },
                modifier = Modifier
                  .size(36.dp)
                  .testTag("btn_delete_drug_${drug.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Delete Drug",
                  tint = Color(0xFFB3261E),
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }

  // --- DIALOGS ---

  // 1. Change PIN Dialog
  if (showChangePinDialog) {
    ChangePinModalDialog(
      onChangePin = { currentCred, newPin -> onChangePin(currentCred, newPin) },
      onDismiss = { showChangePinDialog = false }
    )
  }

  // 2. Change Password Dialog
  if (showChangePasswordDialog) {
    ChangePasswordModalDialog(
      onChangePassword = { currentCred, newPassword -> onChangePassword(currentCred, newPassword) },
      onDismiss = { showChangePasswordDialog = false }
    )
  }

  // 3. Reset Credentials Confirmation Dialog
  if (showResetCredentialsDialog) {
    AlertDialog(
      onDismissRequest = { showResetCredentialsDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(imageVector = Icons.Default.LockReset, contentDescription = null, tint = Color(0xFFB3261E))
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Reset Admin Credentials?")
        }
      },
      text = {
        Text("This will restore the administrative PIN to 8888 and Master Password to Admin@1234. You can customize them again anytime.")
      },
      confirmButton = {
        Button(
          onClick = {
            onResetCredentials()
            showResetCredentialsDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E))
        ) {
          Text("Reset to Defaults")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetCredentialsDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  // 4. Delete Drug Confirmation Dialog
  drugToDelete?.let { drug ->
    AlertDialog(
      onDismissRequest = { drugToDelete = null },
      title = { Text(text = "Delete ${drug.name}?") },
      text = {
        Text("Are you sure you want to remove ${drug.name} (${drug.genericName}) from the clinical formulary? This action cannot be undone unless you restore defaults.")
      },
      confirmButton = {
        Button(
          onClick = {
            onDeleteDrug(drug.id)
            drugToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E))
        ) {
          Text("Delete")
        }
      },
      dismissButton = {
        TextButton(onClick = { drugToDelete = null }) {
          Text("Cancel")
        }
      }
    )
  }

  // 5. Reset Formulary Confirmation Dialog
  if (showResetConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showResetConfirmDialog = false },
      title = { Text(text = "Restore Default Formulary?") },
      text = {
        Text("This will reset all medications to the initial pre-configured clinical drugs and discard any custom entries.")
      },
      confirmButton = {
        Button(
          onClick = {
            onResetDefaults()
            showResetConfirmDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E))
        ) {
          Text("Reset to Defaults")
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetConfirmDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

/**
 * Clean & Secure Dialog for Changing Admin PIN
 */
@Composable
fun ChangePinModalDialog(
  onChangePin: (String, String) -> Boolean,
  onDismiss: () -> Unit
) {
  var currentCredentialInput by remember { mutableStateOf("") }
  var newPinInput by remember { mutableStateOf("") }
  var confirmPinInput by remember { mutableStateOf("") }
  var showCurrentPassword by remember { mutableStateOf(false) }
  var showNewPin by remember { mutableStateOf(false) }
  var pinError by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Default.Pin, contentDescription = null, tint = VibrantPrimary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Change Admin PIN")
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          text = "Enter your current PIN (or Master Password) and select a new 4 to 8 digit numeric security PIN.",
          fontSize = 12.sp,
          color = Color(0xFF49454F)
        )

        OutlinedTextField(
          value = currentCredentialInput,
          onValueChange = { currentCredentialInput = it; pinError = null },
          label = { Text("Current PIN or Password") },
          visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
          trailingIcon = {
            IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
              Icon(
                imageVector = if (showCurrentPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "Toggle Visibility"
              )
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_current_credential_pin")
        )

        OutlinedTextField(
          value = newPinInput,
          onValueChange = {
            if (it.length <= 8 && it.all { ch -> ch.isDigit() }) {
              newPinInput = it
              pinError = null
            }
          },
          label = { Text("New PIN (4-8 digits)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
          visualTransformation = if (showNewPin) VisualTransformation.None else PasswordVisualTransformation(),
          trailingIcon = {
            IconButton(onClick = { showNewPin = !showNewPin }) {
              Icon(
                imageVector = if (showNewPin) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "Toggle Visibility"
              )
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_new_pin")
        )

        OutlinedTextField(
          value = confirmPinInput,
          onValueChange = {
            if (it.length <= 8 && it.all { ch -> ch.isDigit() }) {
              confirmPinInput = it
              pinError = null
            }
          },
          label = { Text("Confirm New PIN") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
          visualTransformation = if (showNewPin) VisualTransformation.None else PasswordVisualTransformation(),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_confirm_pin")
        )

        pinError?.let { err ->
          Text(text = err, color = Color(0xFFB3261E), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (currentCredentialInput.isBlank()) {
            pinError = "Please enter your current PIN or Password"
            return@Button
          }
          if (newPinInput.length < 4) {
            pinError = "New PIN must be at least 4 digits"
            return@Button
          }
          if (newPinInput != confirmPinInput) {
            pinError = "New PIN and confirmation do not match"
            return@Button
          }
          val success = onChangePin(currentCredentialInput, newPinInput)
          if (success) {
            onDismiss()
          } else {
            pinError = "Current PIN or Password is incorrect"
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary),
        modifier = Modifier.testTag("btn_submit_change_pin")
      ) {
        Text("Update PIN")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

/**
 * Clean & Secure Dialog for Changing Admin Master Password
 */
@Composable
fun ChangePasswordModalDialog(
  onChangePassword: (String, String) -> Boolean,
  onDismiss: () -> Unit
) {
  var currentCredentialInput by remember { mutableStateOf("") }
  var newPasswordInput by remember { mutableStateOf("") }
  var confirmPasswordInput by remember { mutableStateOf("") }
  var showCurrentPassword by remember { mutableStateOf(false) }
  var showNewPassword by remember { mutableStateOf(false) }
  var passwordError by remember { mutableStateOf<String?>(null) }

  val strength = remember(newPasswordInput) {
    AdminCredentialsManager.assessPasswordStrength(newPasswordInput)
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Default.VpnKey, contentDescription = null, tint = Color(0xFF6750A4))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Change Master Password")
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          text = "Enter your current Password (or PIN) and create a new master password for full administrative access.",
          fontSize = 12.sp,
          color = Color(0xFF49454F)
        )

        OutlinedTextField(
          value = currentCredentialInput,
          onValueChange = { currentCredentialInput = it; passwordError = null },
          label = { Text("Current Password or PIN") },
          visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
          trailingIcon = {
            IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
              Icon(
                imageVector = if (showCurrentPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "Toggle Visibility"
              )
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_current_credential_password")
        )

        OutlinedTextField(
          value = newPasswordInput,
          onValueChange = { newPasswordInput = it; passwordError = null },
          label = { Text("New Password (min 4 characters)") },
          visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
          trailingIcon = {
            IconButton(onClick = { showNewPassword = !showNewPassword }) {
              Icon(
                imageVector = if (showNewPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "Toggle Visibility"
              )
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_new_password")
        )

        // Password strength meter
        if (newPasswordInput.isNotEmpty()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Strength: ${strength.label}",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = when (strength) {
                PasswordStrength.STRONG -> Color(0xFF0F5132)
                PasswordStrength.MODERATE -> Color(0xFF664D03)
                PasswordStrength.WEAK -> Color(0xFFB3261E)
              }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              for (i in 1..3) {
                Box(
                  modifier = Modifier
                    .size(width = 24.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                      if (i <= strength.score) {
                        when (strength) {
                          PasswordStrength.STRONG -> Color(0xFF0F5132)
                          PasswordStrength.MODERATE -> Color(0xFFEAB308)
                          PasswordStrength.WEAK -> Color(0xFFB3261E)
                        }
                      } else {
                        Color(0xFFE0E0E0)
                      }
                    )
                )
              }
            }
          }
        }

        OutlinedTextField(
          value = confirmPasswordInput,
          onValueChange = { confirmPasswordInput = it; passwordError = null },
          label = { Text("Confirm New Password") },
          visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_confirm_password")
        )

        passwordError?.let { err ->
          Text(text = err, color = Color(0xFFB3261E), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (currentCredentialInput.isBlank()) {
            passwordError = "Please enter current Password or PIN"
            return@Button
          }
          if (newPasswordInput.length < 4) {
            passwordError = "New password must be at least 4 characters"
            return@Button
          }
          if (newPasswordInput != confirmPasswordInput) {
            passwordError = "New password and confirmation do not match"
            return@Button
          }
          val success = onChangePassword(currentCredentialInput, newPasswordInput)
          if (success) {
            onDismiss()
          } else {
            passwordError = "Current Password or PIN is incorrect"
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
        modifier = Modifier.testTag("btn_submit_change_password")
      ) {
        Text("Update Password")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}

/**
 * Clean & Secure Dual-Mode Lock View for Admin Portal (PIN Keypad or Password Mode)
 */
@Composable
fun AdminPasscodeLockView(
  initialLockMode: AdminLockMode = AdminLockMode.PIN,
  onUnlock: (String) -> Boolean,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(if (initialLockMode == AdminLockMode.PIN) 0 else 1) }
  var pinInput by remember { mutableStateOf("") }
  var passwordInput by remember { mutableStateOf("") }
  var isPinVisible by remember { mutableStateOf(false) }
  var isPasswordVisible by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF7F2FA))
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    // Lock Icon Header
    Box(
      modifier = Modifier
        .size(76.dp)
        .clip(CircleShape)
        .background(VibrantPrimaryContainer),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Lock,
        contentDescription = "Admin Lock",
        tint = VibrantPrimary,
        modifier = Modifier.size(40.dp)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Admin Access Restricted",
      fontSize = 22.sp,
      fontWeight = FontWeight.Bold,
      color = Color(0xFF1D1B20),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "Authentication required for formulary management, dosing limits, and system preferences.",
      fontSize = 12.sp,
      color = Color(0xFF49454F),
      textAlign = TextAlign.Center,
      lineHeight = 16.sp,
      modifier = Modifier.padding(horizontal = 8.dp)
    )

    Spacer(modifier = Modifier.height(18.dp))

    // Segmented Mode Switcher (PIN vs Password)
    Surface(
      shape = RoundedCornerShape(50),
      color = Color(0xFFEADDFF),
      modifier = Modifier.width(280.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp)
      ) {
        // PIN Tab
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(50))
            .background(if (selectedTab == 0) VibrantPrimary else Color.Transparent)
            .clickable {
              selectedTab = 0
              errorMessage = null
            }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Pin,
              contentDescription = null,
              tint = if (selectedTab == 0) Color.White else Color(0xFF49454F),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "PIN Keypad",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (selectedTab == 0) Color.White else Color(0xFF49454F)
            )
          }
        }

        // Password Tab
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(50))
            .background(if (selectedTab == 1) Color(0xFF6750A4) else Color.Transparent)
            .clickable {
              selectedTab = 1
              errorMessage = null
            }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Password,
              contentDescription = null,
              tint = if (selectedTab == 1) Color.White else Color(0xFF49454F),
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Password",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = if (selectedTab == 1) Color.White else Color(0xFF49454F)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    if (selectedTab == 0) {
      // --- MODE 0: PIN KEYPAD ---
      Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        val totalDots = 4.coerceAtLeast(pinInput.length)
        for (i in 0 until totalDots.coerceAtMost(8)) {
          val isFilled = i < pinInput.length
          val char = if (isFilled) {
            if (isPinVisible) pinInput[i].toString() else "●"
          } else ""

          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isFilled) VibrantPrimaryContainer else Color.White)
              .border(
                width = if (isFilled) 2.dp else 1.dp,
                color = if (isFilled) VibrantPrimary else Color(0xFFCAC4D0),
                shape = RoundedCornerShape(12.dp)
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = char,
              fontSize = if (isPinVisible) 20.sp else 16.sp,
              fontWeight = FontWeight.Bold,
              color = VibrantPrimary,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }

      errorMessage?.let { err ->
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = err,
          color = Color(0xFFB3261E),
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          textAlign = TextAlign.Center
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Keypad Grid
      Column(
        modifier = Modifier.width(280.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          listOf("1", "2", "3").forEach { digit ->
            KeypadButton(text = digit, onClick = {
              if (pinInput.length < 8) {
                pinInput += digit
                errorMessage = null
              }
            })
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          listOf("4", "5", "6").forEach { digit ->
            KeypadButton(text = digit, onClick = {
              if (pinInput.length < 8) {
                pinInput += digit
                errorMessage = null
              }
            })
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          listOf("7", "8", "9").forEach { digit ->
            KeypadButton(text = digit, onClick = {
              if (pinInput.length < 8) {
                pinInput += digit
                errorMessage = null
              }
            })
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          KeypadIconButton(
            icon = Icons.Default.Refresh,
            contentDesc = "Clear",
            onClick = {
              pinInput = ""
              errorMessage = null
            }
          )

          KeypadButton(text = "0", onClick = {
            if (pinInput.length < 8) {
              pinInput += "0"
              errorMessage = null
            }
          })

          KeypadIconButton(
            icon = Icons.AutoMirrored.Filled.Backspace,
            contentDesc = "Backspace",
            onClick = {
              if (pinInput.isNotEmpty()) {
                pinInput = pinInput.dropLast(1)
                errorMessage = null
              }
            }
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      Button(
        onClick = {
          if (pinInput.isBlank()) {
            errorMessage = "Please enter Admin PIN"
          } else {
            val success = onUnlock(pinInput)
            if (!success) {
              errorMessage = "Incorrect PIN. Please try again."
              pinInput = ""
            }
          }
        },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = VibrantPrimary),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("btn_unlock_admin")
      ) {
        Icon(imageVector = Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Unlock Admin Portal", fontSize = 14.sp, fontWeight = FontWeight.Bold)
      }
    } else {
      // --- MODE 1: PASSWORD TEXT FIELD ---
      Column(
        modifier = Modifier.width(300.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        OutlinedTextField(
          value = passwordInput,
          onValueChange = {
            passwordInput = it
            errorMessage = null
          },
          label = { Text("Master Admin Password") },
          visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
          trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
              Icon(
                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "Toggle Visibility"
              )
            }
          },
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
          keyboardActions = KeyboardActions(onDone = {
            if (passwordInput.isNotBlank()) {
              val success = onUnlock(passwordInput)
              if (!success) {
                errorMessage = "Incorrect Password. Please try again."
                passwordInput = ""
              }
            }
          }),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_admin_password_lock")
        )

        errorMessage?.let { err ->
          Text(
            text = err,
            color = Color(0xFFB3261E),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Button(
          onClick = {
            if (passwordInput.isBlank()) {
              errorMessage = "Please enter Admin Password"
            } else {
              val success = onUnlock(passwordInput)
              if (!success) {
                errorMessage = "Incorrect Password. Please try again."
                passwordInput = ""
              }
            }
          },
          shape = RoundedCornerShape(50),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("btn_unlock_admin_password")
        ) {
          Icon(imageVector = Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = "Unlock with Password", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Info note
    Surface(
      color = Color(0xFFE8DEF8),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Key,
          contentDescription = null,
          tint = Color(0xFF21005D),
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Default Credentials — PIN: 8888 | Password: Admin@1234. Both can be modified in portal settings.",
          fontSize = 10.5.sp,
          color = Color(0xFF21005D),
          lineHeight = 14.sp
        )
      }
    }
  }
}

@Composable
private fun KeypadButton(
  text: String,
  onClick: () -> Unit
) {
  Surface(
    color = VibrantSurface,
    shape = CircleShape,
    border = BorderStroke(1.dp, Color(0xFFCAC4D0)),
    modifier = Modifier
      .size(64.dp)
      .clickable(onClick = onClick)
      .testTag("keypad_$text")
  ) {
    Box(contentAlignment = Alignment.Center) {
      Text(
        text = text,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF1D1B20)
      )
    }
  }
}

@Composable
private fun KeypadIconButton(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  contentDesc: String,
  onClick: () -> Unit
) {
  Surface(
    color = Color(0xFFF3EDF7),
    shape = CircleShape,
    modifier = Modifier
      .size(64.dp)
      .clickable(onClick = onClick)
      .testTag("keypad_${contentDesc.lowercase()}")
  ) {
    Box(contentAlignment = Alignment.Center) {
      Icon(
        imageVector = icon,
        contentDescription = contentDesc,
        tint = Color(0xFF49454F),
        modifier = Modifier.size(22.dp)
      )
    }
  }
}
