package com.f1.quiket.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val QuiketLightColorScheme = lightColorScheme(
    primary = QuiketBlue,
    secondary = QuiketMint,
    surface = QuiketCard,
    background = QuiketSurface,
)

private val QuiketDarkColorScheme = darkColorScheme(
    primary = QuiketBlueDark,
    secondary = QuiketMint,
    surface = QuiketCardDark,
    background = QuiketSurfaceDark,
)

@Composable
fun QuiketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalQuiketSpacing provides QuiketSpacing()) {
        MaterialTheme(
            colorScheme = if (darkTheme) QuiketDarkColorScheme else QuiketLightColorScheme,
            typography = QuiketTypography,
            content = content,
        )
    }
}
