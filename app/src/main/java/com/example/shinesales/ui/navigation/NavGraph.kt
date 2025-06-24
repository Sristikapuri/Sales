package com.example.shinesales.ui.navigation

import LoginScreen
import com.example.shinesales.ui.auth.*
import com.example.shinesales.ui.product.*
import com.example.shinesales.ui.splash.SplashScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.example.shinesales.data.local.AppDatabase
import com.example.shinesales.data.repository.ProductRepository
import com.example.shinesales.viewmodel.ProductViewModel
import com.example.shinesales.viewmodel.ProductViewModelFactory

@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current

    val db = Room.databaseBuilder(context, AppDatabase::class.java, "app_db").build()
    val productRepository = ProductRepository(db.productDao())
    val productViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory(productRepository))

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forgot_password") { ForgotPasswordScreen(navController) }
        composable("profile") { ProfileScreen() }
        composable("product_list") { ProductListScreen(navController, productViewModel) }
        composable("add_edit_product") { AddEditProductScreen(navController, productViewModel) }
        composable("add_edit_product/{productId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            AddEditProductScreen(navController, productViewModel, id)
        }
    }
}
