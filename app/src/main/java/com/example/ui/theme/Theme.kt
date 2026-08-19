package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MyApplicationTheme(
  colorTheme: AppColorTheme = AppColorTheme.VIBRANT_PURPLE,
  darkModePreference: DarkModePreference = DarkModePreference.SYSTEM,
  content: @Composable () -> Unit,
) {
  val isDark = when (darkModePreference) {
    DarkModePreference.SYSTEM -> isSystemInDarkTheme()
    DarkModePreference.LIGHT -> false
    DarkModePreference.DARK -> true
  }

  val lightScheme = lightColorScheme(
    primary = colorTheme.primaryColor,
    onPrimary = Color.White,
    primaryContainer = colorTheme.primaryContainer,
    onPrimaryContainer = colorTheme.onPrimaryContainer,
    secondary = Color(0xFF625B71),
    onSecondary = Color.White,
    secondaryContainer = colorTheme.primaryContainer.copy(alpha = 0.5f),
    onSecondaryContainer = colorTheme.onPrimaryContainer,
    tertiary = Color(0xFF7D5260),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),
    background = Color(0xFFF7F2FA),
    onBackground = Color(0xFF1D1B20),
    surface = Color(0xFFFEF7FF),
    onSurface = Color(0xFF1D1B20),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
  )

  val darkScheme = darkColorScheme(
    primary = colorTheme.doseCardColor,
    onPrimary = colorTheme.doseTextColor,
    primaryContainer = colorTheme.primaryColor,
    onPrimaryContainer = colorTheme.primaryContainer,
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),
    background = Color(0xFF141218),
    onBackground = Color(0xFFE6E0E9),
    surface = Color(0xFF1D1B20),
    onSurface = Color(0xFFE6E0E9),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
  )

  val colorScheme = if (isDark) darkScheme else lightScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
