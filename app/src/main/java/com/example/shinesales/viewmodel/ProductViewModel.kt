package com.example.shinesales.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shinesales.model.ProductModel
import com.example.shinesales.repository.ProductRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val repo: ProductRepositoryImpl) : ViewModel() {

    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        repo.uploadImage(context, imageUri, callback)
    }

    fun addProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        repo.addProduct(model, callback)
    }

    fun deleteProduct(productID: String, callback: (Boolean, String) -> Unit) {
        repo.deleteProduct(productID, callback)
    }

    fun updateProduct(productID: String, productData: MutableMap<String, Any?>, callback: (Boolean, String) -> Unit) {
        repo.updateProduct(productID, productData, callback)
    }

    private val _product = MutableStateFlow<ProductModel?>(null)
    val product: StateFlow<ProductModel?> = _product

    fun getProductByID(productID: String) {
        repo.getProductByID(productID) { data, success, message ->
            if (success) {
                _product.value = data
            } else {
                _product.value = null
            }
        }
    }

    private val _allProducts = MutableStateFlow<List<ProductModel>>(emptyList())
    val allProducts: StateFlow<List<ProductModel>> = _allProducts

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun getAllProduct() {
        _loading.value = true
        repo.getAllProduct { data, success, message ->
            if (success) {
                _loading.value = false
                Log.d("ShineSales", message)
                _allProducts.value = (data ?: emptyList()) as List<ProductModel>
            } else {
                _loading.value = false
                Log.d("ShineSales", message)
                _allProducts.value = emptyList()
            }
        }
    }
}
