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

private val DarkColorScheme =
  darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = ObsidianBackground,
    primaryContainer = EmeraldContainer,
    onPrimaryContainer = OnEmeraldContainer,
    secondary = DiamondCyan,
    onSecondary = ObsidianBackground,
    secondaryContainer = DiamondCyanContainer,
    onSecondaryContainer = OnDiamondContainer,
    tertiary = GoldAccent,
    background = ObsidianBackground,
    surface = ObsidianSurface,
    surfaceVariant = ObsidianSurfaceVariant,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = ObsidianSurfaceBorder,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EmeraldDark,
    onPrimary = TextPrimaryDark,
    primaryContainer = OnEmeraldContainer,
    onPrimaryContainer = EmeraldDark,
    secondary = DiamondCyanDark,
    onSecondary = TextPrimaryDark,
    secondaryContainer = OnDiamondContainer,
    onSecondaryContainer = DiamondCyanDark,
    tertiary = GoldAccent,
    background = SurfaceLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = SurfaceVariantLight,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to sleek gamer dark theme
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
