package com.example.beautyhub.view

// AndroidX Core
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels

// AndroidX Compose Animation
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

// AndroidX Compose Foundation
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

// AndroidX Compose Runtime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// AndroidX Compose UI
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// AndroidX Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Coil Image Loading
import coil.compose.rememberAsyncImagePainter
import coil.compose.AsyncImage

// Material Icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image

import androidx.compose.material.icons.filled.Refresh

// Material3 Components
import androidx.compose.material3.*
import androidx.compose.ui.text.style.TextAlign

// Project Imports
import com.example.beautyhub.R
import com.example.beautyhub.model.ProductModel
import com.example.beautyhub.repository.ProductRepositoryImpl
import com.example.beautyhub.ui.theme.*
import com.example.beautyhub.viewmodel.ProductViewModel

// Define missing constants
private val cardElevation = 4.dp
private val cardShape = RoundedCornerShape(12.dp)
private val imageHeight = 180.dp
private val buttonHeight = 40.dp

// Factory for creating ProductViewModel
class ProductViewModelFactory(
    private val repository: ProductRepositoryImpl
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Empty state view for when there are no products
@Composable
private fun EmptyStateView(
    icon: String,
    title: String,
    message: String,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null,
    primaryColor: Color = PrimaryPeach
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (buttonText != null && onButtonClick != null) {
            Button(
                onClick = onButtonClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                )
            ) {
                Text(buttonText)
            }
        }
    }
}

class ViewProductActivity : ComponentActivity() {
    private val viewModel: ProductViewModel by viewModels(factoryProducer = {
        ProductViewModelFactory(ProductRepositoryImpl())
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeautyHubTheme {
                ViewProductScreen(
                    viewModel = viewModel,
                    onBackClick = { finish() }
                )
            }
        }
    }

    companion object {
        fun start(context: android.content.Context) {
            val intent = Intent(context, ViewProductActivity::class.java)
            context.startActivity(intent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProductScreen(
    viewModel: ProductViewModel,
    onBackClick: () -> Unit = {},
) {
    val context = LocalContext.current

    // Collect UI state from ViewModel
    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    // Local UI state
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<ProductModel?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Derived state
    val products = allProducts

    // Beauty Hub theme colors
    val primaryColor = PrimaryPeach
    val secondaryColor = SecondaryPeach
    val backgroundColor = BackgroundPeach
    val onPrimaryColor = TextOnPeach
    val onSurfaceColor = PrimaryText
    val secondaryTextColor = SecondaryText

    // Using the card styling constants defined at the top of the file

    // Load data when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.getAllProduct()
    }

    // Refresh products
    val refreshProducts: () -> Unit = {
        viewModel.getAllProduct()
    }

    if (showDeleteDialog && productToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Product", fontWeight = FontWeight.Bold, color = Color.Red)
                }
            },
            text = {
                Column {
                    Text("Are you sure you want to delete this product?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = onPrimaryColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(productToDelete!!.productName, fontWeight = FontWeight.SemiBold)
                            Text("₹${String.format("%.0f", productToDelete!!.price)}", color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("This action cannot be undone.", color = Color.Gray, fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.deleteProduct(productToDelete!!.productID) { success, message ->
                                if (success) {
                                    refreshProducts()
                                    // No need for withContext here as we're already on the main thread
                                    // and the callback is already handling the thread switching
                                    Toast.makeText(context, "Product deleted!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                            showDeleteDialog = false
                            productToDelete = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete", color = onPrimaryColor)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    productToDelete = null
                }) {
                    Text("Cancel", color = primaryColor)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Your Beauty Collection",
                        fontWeight = FontWeight.Bold,
                        color = onPrimaryColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = onPrimaryColor
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { refreshProducts() },
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = onPrimaryColor
                        )
                    }

                    // Add product button
                    IconButton(
                        onClick = { AddProductActivity.start(context) },
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Product",
                            tint = onPrimaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = onPrimaryColor,
                    actionIconContentColor = onPrimaryColor
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(backgroundColor)
        ) {
            when {
                loading -> {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = primaryColor)
                    }
                }

                products.isEmpty() -> {
                    // Empty products view
                    EmptyStateView(
                        icon = "💎",
                        title = "No Products Yet",
                        message = "Add your first product to get started",
                        buttonText = "Add Product",
                        onButtonClick = {
                            AddProductActivity.start(context)
                        },
                        primaryColor = primaryColor
                    )
                }

                else -> {
                    // Products list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(products, key = { it.productID }) { product ->
                            ProductCard(
                                product = product,
                                primaryColor = primaryColor,
                                secondaryColor = secondaryColor,
                                backgroundColor = backgroundColor,
                                surfaceColor = SurfacePeach,
                                onEdit = {
                                    val intent = Intent(context, EditProductActivity::class.java).apply {
                                        putExtra("PRODUCT_ID", product.productID)
                                    }
                                    context.startActivity(intent)
                                },
                                onDelete = {
                                    productToDelete = product
                                    showDeleteDialog = true
                                },
                                onViewDetails = {
                                    // Show product details if needed
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductModel,
    primaryColor: Color,
    secondaryColor: Color,
    backgroundColor: Color,
    surfaceColor: Color = SurfacePeach,
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onViewDetails: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Use remember to prevent unnecessary recompositions
    val currentProduct = remember(product) { product }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewDetails?.invoke() },
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = surfaceColor,
            contentColor = primaryColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (product.image.isNotBlank()) {
                    AsyncImage(
                        model = product.image,
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                        error = painterResource(id = R.drawable.ic_launcher_foreground),
                        onError = {
                            // Log error if needed
                        }
                    )
                } else {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = "No Image",
                        tint = secondaryColor.copy(alpha = 0.6f),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Product Details
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Product Name and Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.productName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "$${String.format("%.2f", product.price)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Category
                Text(
                    text = product.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = secondaryColor,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .background(
                            color = secondaryColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = primaryColor.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Edit button
                    if (onEdit != null) {
                        OutlinedButton(
                            onClick = { onEdit() },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = primaryColor
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = primaryColor
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(buttonHeight)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit")
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Delete button
                    if (onDelete != null) {
                        OutlinedButton(
                            onClick = { onDelete() },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Red
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color.Red
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(buttonHeight)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}