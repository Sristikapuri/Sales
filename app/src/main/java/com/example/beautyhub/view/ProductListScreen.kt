package com.example.beautyhub.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.beautyhub.ui.theme.PrimaryPeach

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.beautyhub.model.ProductModel
import com.example.beautyhub.repository.ProductRepositoryImpl
import com.example.beautyhub.ui.theme.*
import com.example.beautyhub.viewmodel.ProductViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen() {
    val context = LocalContext.current
    val repo = remember { ProductRepositoryImpl() }
    val productViewModel = remember { ProductViewModel(repo) }
    
    // State for product list
    val products by productViewModel.allProducts.collectAsState(initial = emptyList())
    val isLoading by productViewModel.loading.collectAsState(initial = true)
    
    // State for delete confirmation dialog
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<ProductModel?>(null) }
    
    // Load products when screen is first displayed
    LaunchedEffect(Unit) {
        productViewModel.getAllProduct()
    }
    
    // Function to navigate to add product screen
    fun navigateToAddProduct() {
        val intent = Intent(context, AddProductActivity::class.java)
        context.startActivity(intent)
    }
    
    // Function to navigate to edit product screen
    fun navigateToEditProduct(productId: String) {
        val intent = Intent(context, EditProductActivity::class.java).apply {
            putExtra("productId", productId)
        }
        context.startActivity(intent)
    }
    
    // Function to handle product deletion
    fun deleteProduct(productId: String) {
        productViewModel.deleteProduct(productId) { success, message ->
            if (success) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                // Refresh the product list
                productViewModel.getAllProduct()
            } else {
                Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "✨ My Beauty Products",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryPeach,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { /* Handle search */ }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search Products",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAddProduct() },
                containerColor = PrimaryPeach,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    Icons.Filled.Add, 
                    contentDescription = "Add Product",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GoldBackground,
                            GoldSurface
                        )
                    )
                )
        ) {
            if (isLoading) {
                // Loading indicator
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = PrimaryPeach)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "✨ Loading your beauty collection...",
                            color = SecondaryText,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else if (products.isEmpty()) {
                // Empty state
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = PrimaryPeach
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No products available",
                            fontSize = 18.sp,
                            color = TextOnGold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { navigateToAddProduct() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryPeach
                            )
                        ) {
                            Text("Add Your First Product")
                        }
                    }
                }
            } else {
                // Product list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onEditClick = { navigateToEditProduct(product.productID) },
                            onDeleteClick = {
                                productToDelete = product
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
            
            // Delete confirmation dialog
            if (showDeleteDialog && productToDelete != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Confirm Deletion") },
                    text = { Text("Are you sure you want to delete ${productToDelete?.productName}?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                productToDelete?.let { deleteProduct(it.productID) }
                                showDeleteDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = { showDeleteDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProductCard(product: ProductModel, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column {
            // Product image
            if (product.image.isNotEmpty()) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.productName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(GoldLight40),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Product placeholder",
                        tint = PrimaryPeach,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
            
            // Product details
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.productName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextOnGold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Text(
                        text = currencyFormat.format(product.price),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPeach
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = product.category,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = product.description,
                    fontSize = 16.sp,
                    color = TextOnGold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Added on: ${dateFormat.format(Date(product.dateAdded))}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onEditClick,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PrimaryPeach
                        )
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = onDeleteClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete")
                    }
                }
            }
        }
    }
}