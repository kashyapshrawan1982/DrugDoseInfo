package com.example.ui.theme

import androidx.compose.ui.graphics.Color

enum class AppColorTheme(
  val displayName: String,
  val primaryColor: Color,
  val primaryContainer: Color,
  val onPrimaryContainer: Color,
  val doseCardColor: Color,
  val doseTextColor: Color,
  val doseSubtextColor: Color
) {
  VIBRANT_PURPLE(
    displayName = "Vibrant Purple",
    primaryColor = Color(0xFF6750A4),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    doseCardColor = Color(0xFFD0BCFF),
    doseTextColor = Color(0xFF21005D),
    doseSubtextColor = Color(0xFF381E72)
  ),
  CLINICAL_TEAL(
    displayName = "Clinical Teal",
    primaryColor = Color(0xFF006A60),
    primaryContainer = Color(0xFF70F5E4),
    onPrimaryContainer = Color(0xFF00201C),
    doseCardColor = Color(0xFF99F6E4),
    doseTextColor = Color(0xFF003731),
    doseSubtextColor = Color(0xFF004D40)
  ),
  ROYAL_OCEAN(
    displayName = "Royal Ocean",
    primaryColor = Color(0xFF155FA0),
    primaryContainer = Color(0xFFD4E3FF),
    onPrimaryContainer = Color(0xFF001C3B),
    doseCardColor = Color(0xFFBAE6FD),
    doseTextColor = Color(0xFF001E3C),
    doseSubtextColor = Color(0xFF0A3A60)
  ),
  EMERALD_GREEN(
    displayName = "Emerald Forest",
    primaryColor = Color(0xFF1B6D24),
    primaryContainer = Color(0xFFB6F3B8),
    onPrimaryContainer = Color(0xFF002204),
    doseCardColor = Color(0xFFBBF7D0),
    doseTextColor = Color(0xFF052E16),
    doseSubtextColor = Color(0xFF14532D)
  ),
  SUNSET_AMBER(
    displayName = "Sunset Amber",
    primaryColor = Color(0xFF934B00),
    primaryContainer = Color(0xFFFFDCC2),
    onPrimaryContainer = Color(0xFF301400),
    doseCardColor = Color(0xFFFED7AA),
    doseTextColor = Color(0xFF431407),
    doseSubtextColor = Color(0xFF7C2D12)
  ),
  RUBY_ROSE(
    displayName = "Ruby Rose",
    primaryColor = Color(0xFFA02B5E),
    primaryContainer = Color(0xFFFFD9E2),
    onPrimaryContainer = Color(0xFF3E001D),
    doseCardColor = Color(0xFFFBCFE8),
    doseTextColor = Color(0xFF500724),
    doseSubtextColor = Color(0xFF831843)
  )
}

enum class DarkModePreference(val displayName: String) {
  SYSTEM("System Default"),
  LIGHT("Always Light"),
  DARK("Always Dark")
}

// Default Vibrant Palette Tokens
val VibrantPrimary = Color(0xFF6750A4)
val VibrantOnPrimary = Color(0xFFFFFFFF)
val VibrantPrimaryContainer = Color(0xFFEADDFF)
val VibrantOnPrimaryContainer = Color(0xFF21005D)

val VibrantSecondary = Color(0xFF625B71)
val VibrantOnSecondary = Color(0xFFFFFFFF)
val VibrantSecondaryContainer = Color(0xFFE8DEF8)
val VibrantOnSecondaryContainer = Color(0xFF1D192B)

val VibrantTertiary = Color(0xFF7D5260)
val VibrantOnTertiary = Color(0xFFFFFFFF)
val VibrantTertiaryContainer = Color(0xFFFFD8E4)
val VibrantOnTertiaryContainer = Color(0xFF31111D)

val VibrantBackground = Color(0xFFF7F2FA)
val VibrantOnBackground = Color(0xFF1D1B20)
val VibrantSurface = Color(0xFFFEF7FF)
val VibrantOnSurface = Color(0xFF1D1B20)
val VibrantSurfaceVariant = Color(0xFFE7E0EC)
val VibrantOnSurfaceVariant = Color(0xFF49454F)

val VibrantOutline = Color(0xFF79747E)
val VibrantOutlineVariant = Color(0xFFCAC4D0)

val VibrantDoseCard = Color(0xFFD0BCFF)
val VibrantDoseText = Color(0xFF21005D)
val VibrantDoseSubtext = Color(0xFF381E72)
val VibrantWarning = Color(0xFFB3261E)
val VibrantWarningContainer = Color(0xFFF9DEDC)
val VibrantSuccess = Color(0xFF1B873F)
val VibrantSuccessContainer = Color(0xFFD4F7DC)

// Dark Theme Variants
val VibrantPrimaryDark = Color(0xFFD0BCFF)
val VibrantOnPrimaryDark = Color(0xFF381E72)
val VibrantPrimaryContainerDark = Color(0xFF4F378B)
val VibrantOnPrimaryContainerDark = Color(0xFFEADDFF)

val VibrantBackgroundDark = Color(0xFF141218)
val VibrantOnBackgroundDark = Color(0xFFE6E0E9)
val VibrantSurfaceDark = Color(0xFF1D1B20)
val VibrantOnSurfaceDark = Color(0xFFE6E0E9)
