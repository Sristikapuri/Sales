import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = {
                email = it
                if (errorMessage.isNotEmpty()) errorMessage = ""
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty() && (email.isBlank() || !isEmailValid(email)),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
                if (errorMessage.isNotEmpty()) errorMessage = ""
            },
            label = { Text("Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty() && password.isBlank(),
            singleLine = true,
            trailingIcon = {
                val icon = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(imageVector = icon, contentDescription = if (isPasswordVisible) "Hide password" else "Show password")
                }
            }
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    email.isBlank() || password.isBlank() -> {
                        errorMessage = "Email and password must not be empty."
                    }
                    !isEmailValid(email) -> {
                        errorMessage = "Invalid email format."
                    }
                    else -> {
                        errorMessage = ""
                        navController.navigate("product_list") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = true
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { navController.navigate("register") }) {
                Text("Register")
            }
            TextButton(onClick = { navController.navigate("forgot_password") }) {
                Text("Forgot Password?")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = androidx.navigation.compose.rememberNavController())
}
