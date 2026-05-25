package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val LightColorScheme =
  lightColorScheme(
    primary = cm_primary,
    onPrimary = cm_onPrimary,
    primaryContainer = cm_primaryContainer,
    onPrimaryContainer = cm_onPrimaryContainer,
    background = cm_background,
    onBackground = cm_onBackground,
    surface = cm_surface,
    onSurface = cm_onSurface,
    surfaceVariant = cm_surfaceVariant,
    onSurfaceVariant = cm_onSurfaceVariant,
    outline = cm_outline,
    outlineVariant = cm_outlineVariant,
    secondaryContainer = cm_secondaryContainer,
    onSecondaryContainer = cm_onSecondaryContainer
  )

private val DarkColorScheme = LightColorScheme // Map both to light for this specific minimalist layout

@Composable
fun DShareTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
