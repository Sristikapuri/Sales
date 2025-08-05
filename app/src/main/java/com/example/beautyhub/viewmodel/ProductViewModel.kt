package com.example.beautyhub.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautyhub.model.ProductModel
import com.example.beautyhub.repository.ProductRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(private val repo: ProductRepositoryImpl) : ViewModel() {
    // Product operations
    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        repo.uploadImage(context, imageUri, callback)
    }

    fun addProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            repo.addProduct(model) { success, message ->
                if (success) {
                    // Refresh products list after adding a new product
                    getAllProduct()
                }
                callback(success, message)
            }
        }
    }

    fun deleteProduct(productID: String, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            repo.deleteProduct(productID) { success, message ->
                if (success) {
                    // Remove from products list
                    _allProducts.value = _allProducts.value.filter { it.productID != productID }
                }
                callback(success, message)
            }
        }
    }

    fun updateProduct(productID: String, productData: MutableMap<String, Any?>, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            repo.updateProduct(productID, productData) { success, message ->
                if (success) {
                    // Refresh the product details and products list
                    getProductByID(productID)
                    getAllProduct()
                }
                callback(success, message)
            }
        }
    }

    // Product state
    private val _product = MutableStateFlow<ProductModel?>(null)
    val product: StateFlow<ProductModel?> = _product.asStateFlow()

    fun getProductByID(productID: String) {
        viewModelScope.launch {
            repo.getProductByID(productID) { data, success, message ->
                if (success) {
                    _product.value = data
                } else {
                    _product.value = null
                    Log.e("ProductViewModel", "Error getting product: $message")
                }
            }
        }
    }

    private val _allProducts = MutableStateFlow<List<ProductModel>>(emptyList())
    val allProducts: StateFlow<List<ProductModel>> = _allProducts.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun getAllProduct() {
        _loading.value = true
        viewModelScope.launch {
            repo.getAllProduct { data, success, message ->
                _loading.value = false
                if (success) {
                    _allProducts.value = (data ?: emptyList()) as List<ProductModel>
                    Log.d("ProductViewModel", "Fetched ${_allProducts.value.size} products")
                } else {
                    Log.e("ProductViewModel", "Error getting products: $message")
                    _allProducts.value = emptyList()
                }
            }
        }
    }

    init {
        // Load initial data
        getAllProduct()
    }
}
