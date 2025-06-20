package com.example.shinesales.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shinesales2.R
import kotlinx.coroutines.delay
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController



@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )
    }
}
@Composable
@Preview(showBackground = true)
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}
