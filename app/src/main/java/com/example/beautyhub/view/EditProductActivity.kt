package com.example.beautyhub.view

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.beautyhub.model.ProductModel
import com.example.beautyhub.repository.ProductRepositoryImpl
import com.example.beautyhub.ui.theme.BeautyHubTheme
import com.example.beautyhub.viewmodel.ProductViewModel
import com.example.beautyhub.ui.theme.DeepPeach
import com.example.beautyhub.ui.theme.PeachPink
import com.example.beautyhub.ui.theme.TextPrimary
import com.example.beautyhub.ui.theme.White
import kotlinx.coroutines.launch

class EditProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productId = intent.getStringExtra("productId") ?: ""
        
        if (productId.isEmpty()) {
            Toast.makeText(this, "Product ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        setContent {
            BeautyHubTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    EditProductScreen(
                        productId = productId,
                        onBack = { finish() },
                        onProductUpdated = {
                            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: String,
    onBack: () -> Unit = {},
    onProductUpdated: () -> Unit = {},
    viewModel: ProductViewModel = remember { ProductViewModel(ProductRepositoryImpl()) }
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isLoading by viewModel.loading.collectAsState()
    
    // Product details state
    var product by remember { mutableStateOf<ProductModel?>(null) }
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var currentImageUrl by remember { mutableStateOf("") }
    
    // Image picker
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri = it }
    }
    
    // Helper function to update the product
    fun updateProduct(imageUrl: String) {
        product?.let { existingProduct ->
            val updatedProduct = existingProduct.copy(
                productName = productName,
                price = productPrice.toDoubleOrNull() ?: 0.0,
                description = productDescription,
                image = imageUrl
            )
            
            viewModel.updateProduct(updatedProduct) { success, message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                if (success) {
                    onProductUpdated()
                }
            }
        }
    }
    
    // Load product details
    LaunchedEffect(productId) {
        viewModel.getProductById(productId) { result ->
            result.onSuccess { loadedProduct ->
                if (loadedProduct != null) {
                    product = loadedProduct
                    productName = loadedProduct.productName
                    productPrice = loadedProduct.price.toString()
                    productDescription = loadedProduct.description
                    currentImageUrl = loadedProduct.image
                } else {
                    Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
                    onBack()
                }
            }.onFailure { error ->
                Toast.makeText(context, "Error loading product: ${error.message}", Toast.LENGTH_SHORT).show()
                onBack()
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Product", color = White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepPeach),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                }
            )
        },
        containerColor = PeachPink.copy(alpha = 0.1f)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = DeepPeach
                )
            } else {
                // Edit form
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Image selection
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray.copy(alpha = 0.3f))
                            .border(2.dp, DeepPeach, RoundedCornerShape(8.dp))
                            .clickable { imagePicker.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "Selected Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else if (currentImageUrl.isNotEmpty()) {
                            AsyncImage(
                                model = currentImageUrl,
                                contentDescription = "Current Product Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Camera,
                                    contentDescription = "Add Image",
                                    tint = DeepPeach,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text("Tap to select image", color = TextPrimary)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Product Name
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Product Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepPeach,
                            focusedLabelColor = DeepPeach
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Product Price
                    OutlinedTextField(
                        value = productPrice,
                        onValueChange = { newValue ->
                            // Only allow numbers and decimal point
                            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                                productPrice = newValue
                            }
                        },
                        label = { Text("Price") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepPeach,
                            focusedLabelColor = DeepPeach
                        ),
                        prefix = { Text("₹") }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Product Description
                    OutlinedTextField(
                        value = productDescription,
                        onValueChange = { productDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeepPeach,
                            focusedLabelColor = DeepPeach
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Update Button
                    Button(
                        onClick = {
                            if (productName.isBlank() || productPrice.isBlank() || productDescription.isBlank()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            
                            val price = productPrice.toDoubleOrNull()
                            if (price == null || price <= 0) {
                                Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            
                            coroutineScope.launch {
                                // If there's a new image, upload it first
                                val currentUri = imageUri
                                if (currentUri != null) {
                                    viewModel.uploadImage(context, currentUri) { imageUrl ->
                                        if (imageUrl != null) {
                                            updateProduct(imageUrl)
                                        } else {
                                            Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    // Use existing image URL
                                    updateProduct(currentImageUrl)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepPeach)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Update Product",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}