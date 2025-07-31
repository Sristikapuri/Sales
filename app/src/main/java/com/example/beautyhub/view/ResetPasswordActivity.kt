package com.example.beautyhub.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.beautyhub.ui.theme.BeautyHubTheme

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeautyHubTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ResetPasswordScreen(onSuccess = { finish() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(onSuccess: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            toastMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Reset Password") })
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Enter your email to receive a reset link")
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (email.isBlank()) {
                            toastMessage = "Please enter your email"
                        } else {
                            isProcessing = true
                            Firebase.auth.sendPasswordResetEmail(email.trim())
                                .addOnCompleteListener { task ->
                                    isProcessing = false
                                    if (task.isSuccessful) {
                                        toastMessage = "Reset email sent. Check your inbox."
                                        onSuccess()
                                    } else {
                                        toastMessage = task.exception?.localizedMessage ?: "Failed to send reset email."
                                    }
                                }
                        }
                    },
                    enabled = !isProcessing,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    } else {
                        Text("Send Reset Email")
                    }
                }
            }
        }
    )
}
