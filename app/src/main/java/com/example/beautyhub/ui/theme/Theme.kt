package com.example.beautyhub.ui.theme

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
    primary = PrimaryDarkPeach,
    secondary = SecondaryPeach,
    tertiary = Cream,
    background = BackgroundPeach.copy(alpha = 0.9f),
    surface = SurfacePeach.copy(alpha = 0.9f),
    onPrimary = TextOnDark,
    onSecondary = TextOnPeach,
    onTertiary = TextOnPeach,
    onBackground = PrimaryText,
    onSurface = PrimaryText,
    primaryContainer = PrimaryDarkPeach.copy(alpha = 0.9f),
    secondaryContainer = SecondaryPeach.copy(alpha = 0.8f),
    onPrimaryContainer = TextOnDark,
    onSecondaryContainer = TextOnPeach
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPeach,
    secondary = SecondaryPeach,
    tertiary = Cream,
    background = BackgroundPeach,
    surface = SurfacePeach,
    onPrimary = TextOnPeach,
    onSecondary = TextOnPeach,
    onTertiary = TextOnPeach,
    onBackground = PrimaryText,
    onSurface = PrimaryText,
    primaryContainer = PrimaryLightPeach,
    secondaryContainer = LightPeach,
    onPrimaryContainer = TextOnPeach,
    onSecondaryContainer = PrimaryText
)

@Composable
fun BeautyHubTheme(
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