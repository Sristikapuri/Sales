package com.example.shinesales.ui.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shinesales.data.model.Product
import com.example.shinesales.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(navController: NavController, viewModel: ProductViewModel) {
    Text("Loaded ProductListScreen", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
    val products = viewModel.products.collectAsState()
    Text("Product count: ${products.value.size}", modifier = Modifier.padding(16.dp)) // Debug count

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add_edit_product")
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Product")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(products.value) { product ->
                ProductItem(
                    product = product,
                    onEdit = {
                        navController.navigate("add_edit_product/${product.id}")
                    },
                    onDelete = {
                        viewModel.deleteProduct(product)
                    }
                )
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text("Price: ₹${product.price}")
                Text("Qty: ${product.quantity}")
            }
            Row {
                TextButton(onClick = onEdit) {
                    Text("Edit")
                }
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
