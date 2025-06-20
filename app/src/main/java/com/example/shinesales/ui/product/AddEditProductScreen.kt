package com.example.shinesales.ui.product

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shinesales.data.model.Product
import com.example.shinesales.viewmodel.ProductViewModel

@Composable
fun AddEditProductScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    productId: Int? = null // null means add, otherwise edit
) {
    val products = viewModel.products.collectAsState()
    val productToEdit = productId?.let { id -> products.value.find { it.id == id } }

    var name by remember { mutableStateOf(productToEdit?.name ?: "") }
    var description by remember { mutableStateOf(productToEdit?.description ?: "") }
    var priceText by remember { mutableStateOf(productToEdit?.price?.toString() ?: "") }
    var quantityText by remember { mutableStateOf(productToEdit?.quantity?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = if (productId == null) "Add Product" else "Edit Product",
            style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        TextField(
            value = priceText,
            onValueChange = { priceText = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        TextField(
            value = quantityText,
            onValueChange = { quantityText = it },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            val price = priceText.toDoubleOrNull() ?: 0.0
            val quantity = quantityText.toIntOrNull() ?: 0

            val product = Product(
                id = productToEdit?.id ?: 0,
                name = name,
                description = description,
                price = price,
                quantity = quantity
            )
            if (productToEdit == null) {
                viewModel.addProduct(product)
            } else {
                viewModel.updateProduct(product)
            }
            navController.navigateUp()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Save")
        }
    }
}
