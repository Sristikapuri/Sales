package com.example.shinesales.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Gold80,
    secondary = GoldLight80,
    tertiary = GoldDark80,
    background = GoldBackground.copy(alpha = 0.9f),
    surface = GoldSurface.copy(alpha = 0.9f),
    onPrimary = TextOnGold,
    onSecondary = TextOnGold,
    onTertiary = Color.White,
    onBackground = TextOnGold,
    onSurface = TextOnGold
)

private val LightColorScheme = lightColorScheme(
    primary = Gold40,
    secondary = GoldLight40,
    tertiary = GoldDark40,
    background = GoldBackground,
    surface = GoldSurface,
    onPrimary = TextOnGold,
    onSecondary = TextOnGold,
    onTertiary = Color.White,
    onBackground = TextOnGold,
    onSurface = TextOnGold
)

@Composable
fun ShineSalesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}