package com.example.shinesales.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinesales.R
import com.example.shinesales.repository.UserRepositoryImpl
import com.example.shinesales.viewmodel.UserViewModel

class ResetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResetPasswordScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen() {
    val repo = remember { UserRepositoryImpl() }
    val userViewModel = remember { UserViewModel(repo) }

    val context = LocalContext.current
    val activity = context as Activity

    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var resetSent by remember { mutableStateOf(false) }

    // Jewelry theme colors
    val primaryColor = Color(0xFF6A1B9A) // Deep Purple for luxury
    val goldColor = Color(0xFFFFD700) // Gold accent
    val backgroundColor = Color(0xFFF8F6FF) // Very light purple
    val cardColor = Color.White
    val textColor = Color(0xFF2D1B3D) // Dark purple
    val placeholderColor = Color(0xFF8E24AA) // Medium purple

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                            activity.finish()
                        }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back to Login",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            backgroundColor,
                            Color(0xFFE8EAF6) // Light purple gradient
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Header
                if (!resetSent) {
                    Text(
                        text = "🔐 Forgot Password? 🔐",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Don't worry! Enter your email and we'll send you a reset link",
                        fontSize = 16.sp,
                        color = placeholderColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp, bottom = 30.dp)
                    )
                } else {
                    Text(
                        text = "✅ Email Sent! ✅",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50), // Success green
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Check your email for password reset instructions",
                        fontSize = 16.sp,
                        color = placeholderColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp, bottom = 30.dp)
                    )
                }

                // Reset Password Illustration
                Image(
                    painter = painterResource(R.drawable.img), // Your jewelry logo or reset password icon
                    contentDescription = null,
                    modifier = Modifier
                        .height(180.dp)
                        .width(180.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                if (!resetSent) {
                    // Reset Password Form Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                                ambientColor = Color.Black.copy(alpha = 0.1f)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Reset Password",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )

                            // Email Field
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                placeholder = { Text("Enter your email address", color = placeholderColor) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = primaryColor
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = primaryColor,
                                    unfocusedBorderColor = Color(0xFFE1BEE7),
                                    focusedLabelColor = primaryColor
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email
                                ),
                                enabled = !isLoading
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Send Reset Link Button
                            Button(
                                onClick = {
                                    if (email.isBlank()) {
                                        Toast.makeText(context, "Please enter your email address", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }

                                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }

                                    isLoading = true

                                    // Using your existing forgetPassword function
                                    userViewModel.forgetPassword(email) { success, message ->
                                        isLoading = false
                                        if (success) {
                                            resetSent = true
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                        } else {
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 8.dp
                                ),
                                enabled = !isLoading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Sending...",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                } else {
                                    Text(
                                        "Send Reset Link",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Back to Login Link
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Remember your password? ",
                                    color = placeholderColor,
                                    fontSize = 14.sp
                                )
                                Text(
                                    "Back to Login",
                                    color = primaryColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.clickable {
                                        val intent = Intent(context, LoginActivity::class.java)
                                        context.startActivity(intent)
                                        activity.finish()
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // Success Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                                ambientColor = Color.Black.copy(alpha = 0.1f)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Success Icon
                            Card(
                                modifier = Modifier.size(80.dp),
                                shape = RoundedCornerShape(40.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                                )
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        "✅",
                                        fontSize = 40.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                "Email Sent Successfully!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                "We've sent password reset instructions to:",
                                fontSize = 14.sp,
                                color = placeholderColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Text(
                                email,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = primaryColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                            )

                            // Back to Login Button
                            Button(
                                onClick = {
                                    val intent = Intent(context, LoginActivity::class.java)
                                    context.startActivity(intent)
                                    activity.finish()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 8.dp
                                )
                            ) {
                                Text(
                                    "Back to Login",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Resend Email Link
                            Text(
                                "Didn't receive email? Resend",
                                color = primaryColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable {
                                    resetSent = false
                                    Toast.makeText(context, "Enter email again to resend", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Additional Help Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = primaryColor.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "💡 Need Help?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        )
                        Text(
                            "Contact our support team if you don't receive the email within 5 minutes",
                            fontSize = 12.sp,
                            color = placeholderColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ResetPasswordScreenPreview() {
    ResetPasswordScreen()
}
