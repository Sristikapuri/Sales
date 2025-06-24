package com.example.shinesales.ui.splash

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.shinesales2.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(2000)
        Log.d("SplashScreen", "Navigating to login")
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }  // Remove splash from backstack
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000))
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash),
                contentDescription = "ShineSales Splash Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1200)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}
