// ProductViewModel.kt
// ViewModel for managing product data and business logic

package com.example.shinesales.viewmodel

import androidx.lifecycle.ViewModel   // ✅ Required import
import androidx.lifecycle.viewModelScope
import com.example.shinesales.data.model.Product
import com.example.shinesales.data.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    val products: StateFlow<List<Product>> =
        repository.allProducts.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addProduct(product: Product) = viewModelScope.launch {
        repository.insert(product)
    }

    fun updateProduct(product: Product) = viewModelScope.launch {
        repository.update(product)
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
        repository.delete(product)
    }
}
