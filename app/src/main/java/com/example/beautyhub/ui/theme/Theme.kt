package com.example.beautyhub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Pink shades for light and dark themes
private val LightPink = Color(0xFFE91E63)   // bright pink for light theme
private val DarkPink = Color(0xFFAD1457)    // darker pink for dark theme

// Silver shades for light and dark themes
private val LightSilver = Color(0xFFC0C0C0)        // classic silver background for light theme
private val DarkSilver = Color(0xFF9E9E9E)         // darker silver background for dark theme
private val LightSilverText = Color(0xFF616161)    // dark gray text on silver background (light)
private val DarkSilverText = Color(0xFFE0E0E0)     // light gray text on silver background (dark)

private val LightColors = lightColorScheme(
    primary = LightPink,
    onPrimary = Color.White,
    background = LightSilver,
    onBackground = LightSilverText,
    surface = LightSilver,
    onSurface = LightSilverText,
    error = Color(0xFFB00020),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = DarkPink,
    onPrimary = Color.White,
    background = DarkSilver,
    onBackground = DarkSilverText,
    surface = DarkSilver,
    onSurface = DarkSilverText,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun BeautyHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(), // Use your custom Typography here if you want
        content = content
    )
}
