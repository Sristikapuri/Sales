package com.example.beautyhub.view

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.beautyhub.model.ProductModel
import com.example.beautyhub.repository.ProductRepositoryImpl
import com.example.beautyhub.ui.theme.PeachPink
import com.example.beautyhub.ui.theme.DeepPeach
import com.example.beautyhub.ui.theme.TextPrimary
import com.example.beautyhub.ui.theme.White
import com.example.beautyhub.viewmodel.ProductViewModel

class AddProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Adjust theme to your app theme
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    primary = PeachPink,
                    secondary = DeepPeach,
                    background = White,
                    surface = White,
                    onPrimary = White
                )
            ) {
                AddProductScreen(
                    onBack = { finish() },
                    onProductAdded = {
                        Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onBack: () -> Unit = {},
    onProductAdded: () -> Unit = {},
    viewModel: ProductViewModel = remember { ProductViewModel(ProductRepositoryImpl()) }
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val primaryColor = PeachPink
    val goldColor = DeepPeach
    val backgroundColor = White

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Product", color = White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = goldColor),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                }
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Image Picker Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clickable(enabled = !isLoading) { imagePickerLauncher.launch("image/*") },
                    colors = CardDefaults.cardColors(containerColor = goldColor.copy(alpha = 0.1f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Selected Product Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.AddPhotoAlternate,
                                    contentDescription = null,
                                    tint = goldColor,
                                    modifier = Modifier.size(60.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Tap to select image", color = goldColor, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Product Name") },
                    placeholder = { Text("E.g., Gold Ring") },
                    leadingIcon = { Icon(Icons.Default.Create, contentDescription = null, tint = goldColor) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }
            item {
                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*\$")))
                            price = it
                    },
                    label = { Text("Price (₹)") },
                    placeholder = { Text("E.g., 499") },
                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null, tint = goldColor) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    )
                )
            }
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("Features, materials, etc.") },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = goldColor) },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    enabled = !isLoading,
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
            }
            item {
                Button(
                    onClick = {
                        when {
                            selectedImageUri == null -> Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                            productName.isBlank() -> Toast.makeText(context, "Please enter product name", Toast.LENGTH_SHORT).show()
                            price.isBlank() -> Toast.makeText(context, "Please enter price", Toast.LENGTH_SHORT).show()
                            description.isBlank() -> Toast.makeText(context, "Please enter description", Toast.LENGTH_SHORT).show()
                            else -> {
                                val priceValue = price.toDoubleOrNull()
                                if (priceValue == null || priceValue <= 0) {
                                    Toast.makeText(context, "Please enter valid price", Toast.LENGTH_SHORT).show()
                                } else {
                                    isLoading = true
                                    viewModel.uploadImage(context, selectedImageUri!!) { imageUrl ->
                                        if (imageUrl != null) {
                                            val product = ProductModel(
                                                productName = productName.trim(),
                                                price = priceValue,
                                                description = description.trim(),
                                                image = imageUrl,
                                                category = "",
                                                dateAdded = System.currentTimeMillis()
                                            )
                                            viewModel.addProduct(product) { success, message ->
                                                isLoading = false
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                                if (success) {
                                                    onProductAdded()
                                                    activity?.finish()
                                                }
                                            }
                                        } else {
                                            isLoading = false
                                            Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = goldColor)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Product", color = Color.White)
                    }
                }
            }
        }
    }
}
