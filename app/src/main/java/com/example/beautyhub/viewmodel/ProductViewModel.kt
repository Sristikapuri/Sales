package com.example.beautyhub.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautyhub.model.ProductModel
import com.example.beautyhub.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val repo: ProductRepository) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadProducts() {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.getAllProducts()
            result.fold(
                onSuccess = { _products.value = it },
                onFailure = { /* Handle error */ }
            )
            _loading.value = false
        }
    }
    
    fun getProductById(id: String, onResult: (Result<ProductModel?>) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.getProductById(id)
            onResult(result)
            _loading.value = false
        }
    }

    fun addProduct(product: ProductModel, onCompleted: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.addProduct(product)
            _loading.value = false
            result.fold(
                onSuccess = { onCompleted(true, "Product added successfully") },
                onFailure = { onCompleted(false, it.localizedMessage ?: "Failed to add product") }
            )
        }
    }

    fun updateProduct(product: ProductModel, onCompleted: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.updateProduct(product)
            _loading.value = false
            result.fold(
                onSuccess = { onCompleted(true, "Product updated successfully") },
                onFailure = { onCompleted(false, it.localizedMessage ?: "Failed to update product") }
            )
        }
    }

    fun deleteProduct(productID: String, onCompleted: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.deleteProduct(productID)
            _loading.value = false
            result.fold(
                onSuccess = { onCompleted(true, "Product deleted") },
                onFailure = { onCompleted(false, it.localizedMessage ?: "Failed to delete product") }
            )
        }
    }

    fun uploadImage(context: Context, uri: Uri, onCompleted: (String?) -> Unit) {
        viewModelScope.launch {
            val result = repo.uploadImage(context, uri)
            onCompleted(result.getOrNull())
        }
    }
}
