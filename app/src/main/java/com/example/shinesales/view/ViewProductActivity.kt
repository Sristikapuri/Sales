package com.example.shinesales.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shinesales.R
import com.example.shinesales.model.ProductModel
import com.example.shinesales.repository.ProductRepositoryImpl
import com.example.shinesales.ui.theme.ShineSalesTheme
import com.example.shinesales.viewmodel.ProductViewModel
import java.text.SimpleDateFormat
import java.util.*

class ViewProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShineSalesTheme {
                ViewProductScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProductScreen() {
    val context = LocalContext.current
    val repo = remember { ProductRepositoryImpl() }
    val viewModel = remember { ProductViewModel(repo) }

    var products by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<ProductModel?>(null) }

    val primaryColor = Color(0xFF6A1B9A)
    val goldColor = Color(0xFFFFD700)
    val backgroundColor = Color(0xFFF8F6FF)

    LaunchedEffect(Unit) {
        loadProducts(repo) { productList, success, message ->
            isLoading = false
            if (success && productList != null) {
                products = productList
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun refreshProducts() {
        isLoading = true
        loadProducts(repo) { productList, success, message ->
            isLoading = false
            if (success && productList != null) {
                products = productList
                Toast.makeText(context, "Products refreshed! ✨", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
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
                        colors = CardDefaults.cardColors(containerColor = backgroundColor),
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
                        viewModel.deleteProduct(productToDelete!!.productID) { success, message ->
                            if (success) {
                                refreshProducts()
                                Toast.makeText(context, "Product deleted!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        showDeleteDialog = false
                        productToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete", color = Color.White)
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
                    Text("Your Jewelry Products", fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        (context as? ComponentActivity)?.finish()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { refreshProducts() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                    IconButton(onClick = {
                        context.startActivity(Intent(context, AddProductActivity::class.java))
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Product", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = primaryColor)
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💎", fontSize = 60.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No Products Added Yet", fontWeight = FontWeight.Bold)
                    Button(onClick = {
                        context.startActivity(Intent(context, AddProductActivity::class.java))
                    }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Your First Product")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(backgroundColor),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        primaryColor = primaryColor,
                        goldColor = goldColor,
                        backgroundColor = backgroundColor,
                        onEdit = {
                            val intent = Intent(context, EditProductActivity::class.java).apply {
                                putExtra("PRODUCT_ID", product.productID)
                                putExtra("PRODUCT_NAME", product.productName)
                                putExtra("PRODUCT_PRICE", product.price)
                                putExtra("PRODUCT_DESCRIPTION", product.description)
                                putExtra("PRODUCT_IMAGE_URL", product.image)
                                putExtra("PRODUCT_CATEGORY", product.category)
                            }
                            context.startActivity(intent)
                        },
                        onDelete = {
                            productToDelete = product
                            showDeleteDialog = true
                        },
                        onViewDetails = {
                            Toast.makeText(context, "Viewing ${product.productName}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductModel,
    primaryColor: Color,
    goldColor: Color,
    backgroundColor: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewDetails() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(backgroundColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (product.image.isNotEmpty()) {
                    AsyncImage(
                        model = product.image,
                        contentDescription = product.productName,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.img),
                        contentDescription = product.productName,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(product.productName, fontWeight = FontWeight.Bold)
                Text(product.description, fontSize = 13.sp, color = Color.Gray, maxLines = 2)
                Text(
                    "₹${String.format("%.0f", product.price)}",
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                if (product.category.isNotEmpty()) {
                    Text(product.category, fontSize = 11.sp, color = goldColor)
                }
                Text(
                    "Added: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(product.dateAdded))}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = primaryColor)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

private fun loadProducts(
    repo: ProductRepositoryImpl,
    callback: (List<ProductModel>?, Boolean, String) -> Unit
) {
    repo.getAllProduct { productList, success, message ->
        callback(productList as List<ProductModel>?, success, message)
    }
}
