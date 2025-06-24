package com.example.shinesales.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    var name by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("john@example.com") }
    var errorMessage by remember { mutableStateOf("") }

    val gold = Color(0xFFFFD700) // Gold
    val silver = Color(0xFFC0C0C0) // Silver
    val darkGray = Color(0xFF2E2E2E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(silver, Color.White, gold),
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Edit Profile",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = darkGray
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Optional Avatar Placeholder
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(listOf(gold, silver))
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("👤", fontSize = 32.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty() && name.isBlank(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = gold,
                unfocusedBorderColor = silver,
                focusedLabelColor = darkGray
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty() && email.isBlank(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = gold,
                unfocusedBorderColor = silver,
                focusedLabelColor = darkGray
            )
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                errorMessage = ""
                if (name.isBlank() || email.isBlank()) {
                    errorMessage = "Name and email are required."
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    errorMessage = "Invalid email format."
                } else {
                    // Save profile logic here
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = gold,
                contentColor = darkGray
            )
        ) {
            Text("Save", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
@Preview(
    showBackground = true,
    name = "Gold & Silver Profile Screen Preview",
    showSystemUi = true
)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
