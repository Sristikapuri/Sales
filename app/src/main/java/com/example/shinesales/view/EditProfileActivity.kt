package com.example.shinesales.view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinesales.repository.UserRepositoryImpl
import com.example.shinesales.ui.theme.*
import com.example.shinesales.viewmodel.UserViewModel

class EditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userId = intent.getStringExtra("USER_ID") ?: ""
        val userName = intent.getStringExtra("USER_NAME") ?: ""
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: ""
        val userPhone = intent.getStringExtra("USER_PHONE") ?: ""
        val userAddress = intent.getStringExtra("USER_ADDRESS") ?: ""

        setContent {
            ShineSalesTheme {
                EditProfileScreen(userId, userName, userEmail, userPhone, userAddress)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    userId: String,
    initialName: String,
    initialEmail: String,
    initialPhone: String,
    initialAddress: String
) {
    val context = LocalContext.current
    val repo = remember { UserRepositoryImpl() }
    val userViewModel = remember { UserViewModel(repo) }

    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var phone by remember { mutableStateOf(initialPhone) }
    var address by remember { mutableStateOf(initialAddress) }
    var isLoading by remember { mutableStateOf(false) }

    val gradientColors = listOf(GrayBackground, GraySurface, GrayCard)

    fun updateProfile() {
        if (name.isBlank() || email.isBlank() || phone.isBlank()) {
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        isLoading = true
        val userData = mutableMapOf<String, Any?>(
            "fullName" to name,
            "email" to email,
            "phone" to phone,
            "address" to address
        )
        
        userViewModel.updateProfile(userId, userData) { success, message ->
            isLoading = false
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            if (success) {
                (context as? Activity)?.finish()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold, color = LightGray) },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = LightGray)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GrayDark80)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(gradientColors))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GrayCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(80.dp),
                            tint = AccentBlue
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Update Your Profile",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = LightGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = GrayCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name", color = MediumGray) },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name", tint = AccentBlue) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentBlue,
                                unfocusedBorderColor = MediumGray,
                                focusedTextColor = LightGray,
                                unfocusedTextColor = LightGray,
                                cursorColor = AccentBlue
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email", color = MediumGray) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = AccentBlue) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentBlue,
                                unfocusedBorderColor = MediumGray,
                                focusedTextColor = LightGray,
                                unfocusedTextColor = LightGray,
                                cursorColor = AccentBlue
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone", color = MediumGray) },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone", tint = AccentBlue) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentBlue,
                                unfocusedBorderColor = MediumGray,
                                focusedTextColor = LightGray,
                                unfocusedTextColor = LightGray,
                                cursorColor = AccentBlue
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Address", color = MediumGray) },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Address", tint = AccentBlue) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentBlue,
                                unfocusedBorderColor = MediumGray,
                                focusedTextColor = LightGray,
                                unfocusedTextColor = LightGray,
                                cursorColor = AccentBlue
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { updateProfile() },
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = LightGray, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Update Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
