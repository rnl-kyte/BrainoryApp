package com.devbytes.brainory.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary          = BrainoryTeal,
    onPrimary        = CardBg,
    primaryContainer = GradientTop,
    secondary        = BrainoryYellow,
    onSecondary      = TextPrimary,
    background       = SurfaceLight,
    onBackground     = TextPrimary,
    surface          = CardBg,
    onSurface        = TextPrimary,
    surfaceVariant   = SurfaceLight,
    error            = ErrorRed,
)

@Composable
fun BrainoryAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = BrainoryTypography,
        content     = content
    )
}