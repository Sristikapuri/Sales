package com.example.beautyhub.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart

import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.beautyhub.model.ProductModel
import com.example.beautyhub.repository.ProductRepositoryImpl
import com.example.beautyhub.ui.theme.*
import com.example.beautyhub.viewmodel.ProductViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

class AddProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeautyHubTheme {
                AddProductScreen()
            }
        }
    }

    companion object {
        fun start(context: android.content.Context) {
            val intent = Intent(context, AddProductActivity::class.java)
            context.startActivity(intent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    val repo = remember { ProductRepositoryImpl() }
    val viewModel = remember { ProductViewModel(repo) }
    val coroutineScope = rememberCoroutineScope()

    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf("") }
    val categories = listOf("Skin Care", "Hair Care", "Makeup", "Fragrance", "Other")
    var expanded by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            uri -> uri?.let { imageUri = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("✨ Add New Product", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryPeach)
            )
        },
        containerColor = BackgroundPeach
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Image Picker
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfacePeach)
                    .clickable { imagePicker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Product Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "No Image",
                            tint = PrimaryPeach.copy(alpha = 0.5f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap to add product image",
                            color = PrimaryPeach
                        )
                    }

                }
            }

            // Name
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = PrimaryPeach) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPeach,
                    focusedLabelColor = PrimaryPeach,
                    cursorColor = PrimaryPeach
                )
            )

            // Price
            OutlinedTextField(
                value = price,
                onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*$"))) price = it },
                label = { Text("Price (₹)") },
                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = PrimaryPeach) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPeach,
                    focusedLabelColor = PrimaryPeach,
                    cursorColor = PrimaryPeach
                )
            )

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = PrimaryPeach) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                singleLine = false,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPeach,
                    focusedLabelColor = PrimaryPeach,
                    cursorColor = PrimaryPeach
                )
            )

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPeach,
                        focusedLabelColor = PrimaryPeach,
                        cursorColor = PrimaryPeach
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            // View Products
            OutlinedButton(
                onClick = { ViewProductActivity.start(context) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PrimaryPeach,
                    containerColor = Color.White
                )
            ) {
                Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("View All Products", fontSize = 16.sp)
            }

            // Add Product Button
            Button(
                onClick = {
                    if (validateForm(productName, price, description, imageUri, selectedCategory, context)) {
                        isLoading = true
                        val currentUri = imageUri

                        coroutineScope.launch {
                            val imageUrl = try {
                                if (currentUri != null) {
                                    suspendCancellableCoroutine<String?> { continuation ->
                                        viewModel.uploadImage(context, currentUri) { url ->
                                            continuation.resume(url)
                                        }
                                    }
                                } else ""
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                    isLoading = false
                                }
                                return@launch
                            }

                            val product = ProductModel(
                                productID = UUID.randomUUID().toString(),
                                productName = productName,
                                price = price.toDouble(),
                                description = description,
                                image = imageUrl ?: "",
                                category = selectedCategory
                            )

                            viewModel.addProduct(product) { success, message ->
                                coroutineScope.launch {
                                    withContext(Dispatchers.Main) {
                                        isLoading = false
                                        if (success) {
                                            Toast.makeText(context, "Product added!", Toast.LENGTH_SHORT).show()
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                activity?.finish()
                                            }, 1500)
                                        } else {
                                            Toast.makeText(context, "Failed: $message", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPeach,
                    contentColor = Color.White
                ),
                enabled = !isLoading && productName.isNotBlank() && price.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Add Product", fontSize = 16.sp)
                }
            }
        }
    }
}

private fun validateForm(
    productName: String,
    price: String,
    description: String,
    imageUri: Uri?,
    category: String,
    context: android.content.Context
): Boolean {
    return when {
        productName.isBlank() -> {
            Toast.makeText(context, "Please enter product name", Toast.LENGTH_SHORT).show()
            false
        }
        price.isBlank() -> {
            Toast.makeText(context, "Please enter price", Toast.LENGTH_SHORT).show()
            false
        }
        description.isBlank() -> {
            Toast.makeText(context, "Please enter description", Toast.LENGTH_SHORT).show()
            false
        }
        imageUri == null -> {
            Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
            false
        }
        category.isBlank() -> {
            Toast.makeText(context, "Please select a category", Toast.LENGTH_SHORT).show()
            false
        }
        else -> true
    }
}
