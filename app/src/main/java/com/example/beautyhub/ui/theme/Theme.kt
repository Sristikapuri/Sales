package com.example.beautyhub.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White
import com.example.beautyhub.ui.theme.DeepPeach
import com.example.beautyhub.ui.theme.PeachPink
import com.example.beautyhub.ui.theme.TextPrimary

private val LightColors = lightColorScheme(
    primary = PeachPink,
    secondary = DeepPeach,
    background = White,
    surface = White,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun BeautyHubTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
