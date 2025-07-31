package com.example.beautyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.beautyhub.ui.theme.BeautyHubTheme
import com.example.beautyhub.view.AddProductActivity
import com.example.beautyhub.view.DashboardActivity
import com.example.beautyhub.view.DashboardScreen
import com.example.beautyhub.view.EditProductActivity
import com.example.beautyhub.view.LoginScreen
import com.example.beautyhub.view.RegisterScreen
import com.example.beautyhub.view.ResetPasswordActivity
import com.example.beautyhub.view.SplashScreen
import com.example.beautyhub.view.ViewProductActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BeautyHubTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    BeautyHubNavGraph(navController)
                }
            }
        }
    }
}

@Composable
fun BeautyHubNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("dashboard") {
            DashboardScreen(
                navController = navController,
                onLogout = {
                    // You can navigate back or finish activity accordingly
                    // For example:
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }
        
        // Add product, view product, edit product and reset password routes
        composable("add_product") {
            val context = navController.context
            val intent = android.content.Intent(context, AddProductActivity::class.java)
            context.startActivity(intent)
        }
        
        composable("view_product") {
            val context = navController.context
            val intent = android.content.Intent(context, ViewProductActivity::class.java)
            context.startActivity(intent)
        }
        
        composable("edit_product/{productId}") { backStackEntry ->
            val context = navController.context
            val productId = backStackEntry.arguments?.getString("productId")
            val intent = android.content.Intent(context, EditProductActivity::class.java)
            intent.putExtra("productId", productId)
            context.startActivity(intent)
        }
        
        composable("reset_password") {
            val context = navController.context
            val intent = android.content.Intent(context, ResetPasswordActivity::class.java)
            context.startActivity(intent)
        }
    }
}
