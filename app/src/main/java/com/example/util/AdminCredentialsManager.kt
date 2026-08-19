package com.example.util

import android.content.Context
import android.content.SharedPreferences

enum class AdminLockMode {
  PIN,
  PASSWORD
}

enum class PasswordStrength(val label: String, val score: Int) {
  WEAK("Weak", 1),
  MODERATE("Moderate", 2),
  STRONG("Strong", 3)
}

object AdminCredentialsManager {
  private const val PREFS_NAME = "pediatric_calc_admin_security"
  private const val KEY_ADMIN_PIN = "admin_security_pin"
  private const val KEY_ADMIN_PASSWORD = "admin_security_password"
  private const val KEY_LOCK_MODE = "admin_lock_mode"

  const val DEFAULT_PIN = "8888"
  const val DEFAULT_PASSWORD = "Admin@1234"

  private var inMemoryPin: String = DEFAULT_PIN
  private var inMemoryPassword: String = DEFAULT_PASSWORD
  private var inMemoryLockMode: AdminLockMode = AdminLockMode.PIN

  private fun getPrefs(context: Context?): SharedPreferences? {
    return context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  }

  fun getPin(context: Context? = null): String {
    val prefs = getPrefs(context)
    return prefs?.getString(KEY_ADMIN_PIN, inMemoryPin) ?: inMemoryPin
  }

  fun savePin(newPin: String, context: Context? = null) {
    inMemoryPin = newPin
    getPrefs(context)?.edit()?.putString(KEY_ADMIN_PIN, newPin)?.apply()
  }

  fun getPassword(context: Context? = null): String {
    val prefs = getPrefs(context)
    return prefs?.getString(KEY_ADMIN_PASSWORD, inMemoryPassword) ?: inMemoryPassword
  }

  fun savePassword(newPassword: String, context: Context? = null) {
    inMemoryPassword = newPassword
    getPrefs(context)?.edit()?.putString(KEY_ADMIN_PASSWORD, newPassword)?.apply()
  }

  fun getLockMode(context: Context? = null): AdminLockMode {
    val prefs = getPrefs(context)
    val modeName = prefs?.getString(KEY_LOCK_MODE, inMemoryLockMode.name) ?: inMemoryLockMode.name
    return try {
      AdminLockMode.valueOf(modeName)
    } catch (e: Exception) {
      AdminLockMode.PIN
    }
  }

  fun saveLockMode(mode: AdminLockMode, context: Context? = null) {
    inMemoryLockMode = mode
    getPrefs(context)?.edit()?.putString(KEY_LOCK_MODE, mode.name)?.apply()
  }

  fun resetToDefaults(context: Context? = null) {
    inMemoryPin = DEFAULT_PIN
    inMemoryPassword = DEFAULT_PASSWORD
    inMemoryLockMode = AdminLockMode.PIN
    getPrefs(context)?.edit()?.clear()?.apply()
  }

  fun assessPasswordStrength(password: String): PasswordStrength {
    if (password.length < 6) return PasswordStrength.WEAK
    var score = 0
    if (password.length >= 8) score++
    if (password.any { it.isUpperCase() } && password.any { it.isLowerCase() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++

    return when {
      score >= 3 -> PasswordStrength.STRONG
      score >= 2 -> PasswordStrength.MODERATE
      else -> PasswordStrength.WEAK
    }
  }
}
