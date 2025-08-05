package com.example.beautyhub.view


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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.launch
import com.example.beautyhub.R
import com.example.beautyhub.model.UserModel
import com.example.beautyhub.repository.UserRepositoryImpl
import com.example.beautyhub.viewmodel.UserViewModel
import com.example.beautyhub.ui.theme.*

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistrationBody()
        }
    }
}

@Composable
fun RegistrationBody() {
    val repo = remember { UserRepositoryImpl() }
    val userViewModel = remember { UserViewModel(repo) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var agreeToTerms by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val activity = context as? Activity
    val scrollState = rememberScrollState()

    // Beauty Hub theme colors
    val primaryColor = PrimaryPeach
    val secondaryColor = PrimaryLightPeach
    val backgroundColor = BackgroundPeach
    val surfaceColor = SurfacePeach
    val onPrimaryColor = TextOnPeach
    val onSurfaceColor = PrimaryText
    val secondaryTextColor = SecondaryText
    val errorColor = ErrorRed

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
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
                    .padding(padding)
                    .padding(24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Header
                Text(
                    text = "✨ Beautiful skin starts here ✨",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = onSurfaceColor
                )
                Text(
                    text = " Create account for beautiful skin ",
                    fontSize = 16.sp,
                    color = secondaryTextColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                Image(
                    painter = painterResource(R.drawable.bbbb),
                    contentDescription = null,
                    modifier = Modifier
                        .height(180.dp)
                        .width(180.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Registration Form Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = Color.Black.copy(alpha = 0.1f)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceColor)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Create Account",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurfaceColor,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        // Full Name Field
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = onPrimaryColor
                                )
                            },
                            placeholder = {
                                Text("Full Name", color = secondaryTextColor)
                            },
                            value = fullName,
                            onValueChange = { input ->
                                fullName = input
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = secondaryTextColor.copy(alpha = 0.5f),
                                cursorColor = primaryColor,
                                focusedLabelColor = primaryColor,
                                unfocusedLabelColor = secondaryTextColor,
                                focusedTextColor = onSurfaceColor,
                                unfocusedTextColor = onSurfaceColor
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // Email Field
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = null,
                                    tint = onPrimaryColor
                                )
                            },
                            placeholder = {
                                Text("abc@gmail.com", color = secondaryTextColor)
                            },
                            value = email,
                            onValueChange = { input ->
                                email = input
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = secondaryTextColor.copy(alpha = 0.5f),
                                cursorColor = primaryColor,
                                focusedLabelColor = primaryColor,
                                unfocusedLabelColor = secondaryTextColor,
                                focusedTextColor = onSurfaceColor,
                                unfocusedTextColor = onSurfaceColor
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // Password Field
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            visualTransformation = if (passwordVisibility) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = onPrimaryColor
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisibility = !passwordVisibility }
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            if (passwordVisibility)
                                                R.drawable.outline_visibility_24
                                            else
                                                R.drawable.baseline_visibility_off_24
                                        ),
                                        contentDescription = null,
                                        tint = onPrimaryColor
                                    )
                                }
                            },
                            placeholder = {
                                Text("Password", color = secondaryTextColor)
                            },
                            value = password,
                            onValueChange = { input ->
                                password = input
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = secondaryTextColor.copy(alpha = 0.5f),
                                cursorColor = primaryColor,
                                focusedLabelColor = primaryColor,
                                unfocusedLabelColor = secondaryTextColor,
                                focusedTextColor = onSurfaceColor,
                                unfocusedTextColor = onSurfaceColor
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // Confirm Password Field
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = onPrimaryColor
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            if (confirmPasswordVisibility)
                                                R.drawable.outline_visibility_24
                                            else
                                                R.drawable.baseline_visibility_off_24
                                        ),
                                        contentDescription = null,
                                        tint = onPrimaryColor
                                    )
                                }
                            },
                            placeholder = {
                                Text("Confirm Password", color = secondaryTextColor)
                            },
                            value = confirmPassword,
                            onValueChange = { input ->
                                confirmPassword = input
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = secondaryTextColor.copy(alpha = 0.5f),
                                cursorColor = primaryColor,
                                focusedLabelColor = primaryColor,
                                unfocusedLabelColor = secondaryTextColor,
                                focusedTextColor = onSurfaceColor,
                                unfocusedTextColor = onSurfaceColor
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // Phone Number Field
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone
                            ),
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = onPrimaryColor
                                )
                            },
                            placeholder = {
                                Text("Phone Number", color = secondaryTextColor)
                            },
                            value = phoneNumber,
                            onValueChange = { input ->
                                phoneNumber = input
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = secondaryTextColor.copy(alpha = 0.5f),
                                cursorColor = primaryColor,
                                focusedLabelColor = primaryColor,
                                unfocusedLabelColor = secondaryTextColor,
                                focusedTextColor = onSurfaceColor,
                                unfocusedTextColor = onSurfaceColor
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        // Address Field
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(),
                            leadingIcon = {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = onPrimaryColor
                                )
                            },
                            placeholder = {
                                Text("Enter Your Address", color = secondaryTextColor)
                            },
                            value = address,
                            onValueChange = { input ->
                                address = input
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = secondaryTextColor.copy(alpha = 0.5f),
                                cursorColor = primaryColor,
                                focusedLabelColor = primaryColor,
                                unfocusedLabelColor = secondaryTextColor,
                                focusedTextColor = onSurfaceColor,
                                unfocusedTextColor = onSurfaceColor
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Terms and Conditions Checkbox
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = agreeToTerms,
                                onCheckedChange = { checked ->
                                    agreeToTerms = checked
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = primaryColor,
                                    checkmarkColor = Color.White,
                                    uncheckedColor = secondaryTextColor
                                )
                            )
                            Text(
                                text = "I agree to the Terms and Conditions",
                                modifier = Modifier.padding(start = 8.dp),
                                color = onSurfaceColor,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Register Button
                        Button(
                            onClick = {
                                // Validation
                                if (fullName.isBlank() || email.isBlank() || phoneNumber.isBlank() ||
                                    password.isBlank() || confirmPassword.isBlank() || address.isBlank()) {
                                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                if (password != confirmPassword) {
                                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                if (!agreeToTerms) {
                                    Toast.makeText(context, "Please agree to terms and conditions", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                userViewModel.register(email, password) { success, message, userId ->
                                    if (success) {
                                        val userModel = UserModel(
                                            userId, fullName, email, "", phoneNumber, address
                                        )
                                        userViewModel.addUserToDatabase(userId, userModel) { success, message ->
                                            if (success) {
                                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                                val intent = Intent(context, LoginActivity::class.java)
                                                context.startActivity(intent)
                                                activity?.finish()
                                            } else {
                                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                            }
                                        }
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
                            )
                        ) {
                            Text(
                                "Create Account",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = onPrimaryColor
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Login Link
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Already have an account? ",
                                color = secondaryTextColor,
                                fontSize = 14.sp
                            )
                            Text(
                                "Login Now",
                                color = primaryColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable {
                                    val intent = Intent(context, LoginActivity::class.java)
                                    context.startActivity(intent)
                                    activity?.finish()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Social Registration Options
                Text(
                    text = "Or register with",
                    color = secondaryTextColor,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.beauty),
                        contentDescription = null,
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                            .clickable {
                                Toast.makeText(context, "Google registration coming soon!", Toast.LENGTH_SHORT).show()
                            }
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Image(
                        painter = painterResource(R.drawable.beauty),
                        contentDescription = null,
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                            .clickable {
                                Toast.makeText(context, "Facebook registration coming soon!", Toast.LENGTH_SHORT).show()
                            }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview
@Composable
fun RegistrationPreviewBody() {
    RegistrationBody()
}
