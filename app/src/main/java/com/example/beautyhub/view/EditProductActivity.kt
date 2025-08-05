package com.example.beautyhub.view

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource  // ADD THIS IMPORT
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.beautyhub.R  // ADD THIS IMPORT
import com.example.beautyhub.model.ProductModel
import com.example.beautyhub.repository.ProductRepositoryImpl
import com.example.beautyhub.ui.theme.BackgroundPeach
import com.example.beautyhub.ui.theme.BeautyHubTheme
import com.example.beautyhub.ui.theme.PrimaryLightPeach
import com.example.beautyhub.ui.theme.PrimaryPeach
import com.example.beautyhub.ui.theme.PrimaryText
import com.example.beautyhub.ui.theme.SecondaryText
import com.example.beautyhub.ui.theme.SurfacePeach
import com.example.beautyhub.ui.theme.TextOnPeach
import com.example.beautyhub.viewmodel.ProductViewModel

class EditProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get product data from intent
        val productId = intent.getStringExtra("PRODUCT_ID") ?: ""
        val productName = intent.getStringExtra("PRODUCT_NAME") ?: ""
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)
        val productDescription = intent.getStringExtra("PRODUCT_DESCRIPTION") ?: ""
        val productImageUrl = intent.getStringExtra("PRODUCT_IMAGE_URL") ?: ""

        setContent {
            BeautyHubTheme {
                EditProductScreen(
                    productId = productId,
                    initialName = productName,
                    initialPrice = productPrice,
                    initialDescription = productDescription,
                    initialImageUrl = productImageUrl
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: String,
    initialName: String,
    initialPrice: Double,
    initialDescription: String,
    initialImageUrl: String
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val repo = remember { ProductRepositoryImpl() }
    val viewModel = remember { ProductViewModel(repo) }

    var productName by remember { mutableStateOf(initialName) }
    var price by remember { mutableStateOf(if (initialPrice > 0) initialPrice.toString() else "") }
    var description by remember { mutableStateOf(initialDescription) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var currentImageUrl by remember { mutableStateOf(initialImageUrl) }
    var isLoading by remember { mutableStateOf(false) }

    // Beauty Hub theme colors
    val primaryColor = PrimaryPeach
    val secondaryColor = PrimaryLightPeach
    val backgroundColor = BackgroundPeach
    val surfaceColor = SurfacePeach
    val onPrimaryColor = TextOnPeach
    val onSurfaceColor = PrimaryText
    val secondaryTextColor = SecondaryText

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (productId.isEmpty()) "✨ Add New Product" else "✏️ Edit Product",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = primaryColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Update your beauty product details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    Text(
                        "Update product information",
                        fontSize = 14.sp,
                        color = secondaryTextColor
                    )
                }
            }

            // Image Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Product Image",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = primaryColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                2.dp,
                                primaryColor.copy(alpha = 0.5f),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            selectedImageUri != null -> {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "New Image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            currentImageUrl.isNotEmpty() -> {
                                AsyncImage(
                                    model = currentImageUrl,
                                    contentDescription = "Current Image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop,
                                    fallback = painterResource(R.drawable.bp),
                                    error = painterResource(R.drawable.bbbb)
                                )
                            }
                            else -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Add Photo",
                                        tint = primaryColor,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "Tap to add photo",
                                        color = primaryColor,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "💄 Product Details",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryText,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Product Name
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Product Name") },
                        placeholder = { Text("e.g., Gold Ring, Diamond Necklace") },
                        leadingIcon = {
                            Icon(Icons.Default.Create, contentDescription = null, tint = primaryColor)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor,
                            cursorColor = primaryColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading,
                        singleLine = true
                    )

                    // Price
                    OutlinedTextField(
                        value = price,
                        onValueChange = { newValue ->
                            // Only allow digits and decimal point
                            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                                price = newValue
                            }
                        },
                        label = { Text("Price (₹)") },
                        placeholder = { Text("Enter price in rupees") },
                        leadingIcon = {
                            Icon(Icons.Default.Home, contentDescription = null, tint = primaryColor)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor,
                            cursorColor = primaryColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        enabled = !isLoading,
                        singleLine = true
                    )

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        placeholder = { Text("Describe material, design, condition, size...") },
                        leadingIcon = {
                            Icon(Icons.Default.Info, contentDescription = null, tint = primaryColor)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor,
                            focusedLabelColor = primaryColor,
                            cursorColor = primaryColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading
                    )
                }
            }

            // Update Button
            Button(
                onClick = {
                    when {
                        productName.isBlank() -> {
                            Toast.makeText(context, "Please enter product name", Toast.LENGTH_SHORT).show()
                        }
                        price.isBlank() -> {
                            Toast.makeText(context, "Please enter price", Toast.LENGTH_SHORT).show()
                        }
                        description.isBlank() -> {
                            Toast.makeText(context, "Please enter description", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            try {
                                val priceValue = price.toDouble()
                                if (priceValue <= 0) {
                                    Toast.makeText(context, "Please enter a valid price greater than 0", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                isLoading = true

                                if (selectedImageUri != null) {
                                    // Upload new image first
                                    viewModel.uploadImage(context, selectedImageUri!!) { imageUrl ->
                                        if (imageUrl != null) {
                                            updateProduct(
                                                viewModel, productId, productName.trim(), priceValue,
                                                description.trim(), imageUrl, context, activity
                                            ) { isLoading = false }
                                        } else {
                                            isLoading = false
                                            Toast.makeText(context, "Failed to upload image. Please try again.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    // Keep existing image
                                    updateProduct(
                                        viewModel, productId, productName.trim(), priceValue,
                                        description.trim(), currentImageUrl, context, activity
                                    ) { isLoading = false }
                                }
                            } catch (e: NumberFormatException) {
                                Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = onPrimaryColor,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Updating Product...")
                } else {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Update Product", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun updateProduct(
    viewModel: ProductViewModel,
    productId: String,
    name: String,
    price: Double,
    description: String,
    imageUrl: String,
    context: android.content.Context,
    activity: Activity?,
    onComplete: () -> Unit
) {
    val updateData = mutableMapOf<String, Any?>(
        "name" to name,
        "price" to price,
        "description" to description,
        "imageUrl" to imageUrl
    )

    viewModel.updateProduct(productId, updateData) { success, message ->
        onComplete()
        if (success) {
            Toast.makeText(context, "Product updated successfully! ✨", Toast.LENGTH_LONG).show()
            activity?.finish()
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}
