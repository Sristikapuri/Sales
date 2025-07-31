package com.example.beautyhub.view

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.beautyhub.ui.theme.PeachPink
import com.example.beautyhub.ui.theme.DeepPeach
import com.example.beautyhub.ui.theme.White
import com.example.beautyhub.ui.theme.TextPrimary
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            DashboardScreen(navController = navController, onLogout = { finish() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: androidx.navigation.NavController,
    onLogout: () -> Unit
) {
    val auth = Firebase.auth
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val primaryColor = PeachPink
    val accentColor = DeepPeach

    Scaffold(
        containerColor = primaryColor,
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", color = White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = accentColor),
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            auth.signOut()
                            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") {
                                popUpTo("dashboard") { inclusive = true }
                            }
                            onLogout()
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = White)
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Welcome to BeautyHub!",
                    fontSize = 24.sp,
                    color = TextPrimary
                )
                Button(
                    onClick = { navController.navigate("add_product") },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Product", tint = White)
                    Spacer(Modifier.width(8.dp))
                    Text("Add Product", color = White)
                }
                Button(
                    onClick = { navController.navigate("view_product") },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Icon(Icons.Default.List, contentDescription = "View Products", tint = White)
                    Spacer(Modifier.width(8.dp))
                    Text("View Products", color = White)
                }
            }
        }
    }
}
