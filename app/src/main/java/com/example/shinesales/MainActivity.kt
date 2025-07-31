package com.example.shinesales

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.shinesales.view.LoginActivity
import com.example.shinesales.ui.theme.ShineSalesTheme
import com.example.shinesales.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShineSalesTheme {
                SplashScreen(
                    onNavigateToLogin = {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    var logoVisible by remember { mutableStateOf(false) }
    var textVisible by remember { mutableStateOf(false) }

    // Rotation animation for decorative elements
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    // Scale animation for logo
    val logoScale by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "logoScale"
    )

    // Start animations with staggered delays
    LaunchedEffect(true) {
        visible = true
        delay(300)
        logoVisible = true
        delay(500)
        textVisible = true
        delay(1200) // Total 2 seconds
        onNavigateToLogin()
    }

    // Gradient colors for dark gray theme
    val gradientColors = listOf(
        GrayBackground, // Dark Background
        GraySurface,   // Dark Surface
        GrayCard,      // Card Background
        Gray40,        // Medium Gray
        Gray80         // Primary Dark Gray
    )

    val accentGradient = listOf(
        AccentBlue,    // Light Blue accent
        Gray80,        // Primary Dark Gray
        GrayDark80     // Very Dark Gray
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background decorative circles
        if (visible) {
            // Large rotating circle
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .rotate(rotationAngle)
                    .alpha(0.1f)
                    .background(
                        Color.White,
                        CircleShape
                    )
            )

            // Smaller rotating circle
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .rotate(-rotationAngle * 0.7f)
                    .alpha(0.15f)
                    .background(
                        brush = Brush.radialGradient(accentGradient),
                        CircleShape
                    )
            )
        }

        // Main content
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(800))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                // Logo with scale animation and gradient background
                Card(
                    modifier = Modifier
                        .scale(logoScale)
                        .size(140.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = GrayCard
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = accentGradient
                                ),
                                shape = CircleShape
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img5),
                            contentDescription = "Shine Sales Logo",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Animated title text
                AnimatedVisibility(
                    visible = textVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(600)
                    ) + fadeIn(animationSpec = tween(600))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Main title with gradient effect
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = GrayCard.copy(alpha = 0.9f)
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "✨ Shine Sales ✨",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = LightGray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(
                                    horizontal = 24.dp,
                                    vertical = 12.dp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Subtitle with background
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = AccentBlue.copy(alpha = 0.9f)
                            )
                        ) {
                            Text(
                                text = "💎 Jewellery Shopping Platform 💎",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(
                                    horizontal = 20.dp,
                                    vertical = 8.dp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Loading indicator
                        CircularProgressIndicator(
                            color = accentGradient[0],
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}
