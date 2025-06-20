package com.example.shinesales.data.repository

import com.example.shinesales.data.local.ProductDao
import com.example.shinesales.data.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun insert(product: Product) = productDao.insertProduct(product)
    suspend fun update(product: Product) = productDao.updateProduct(product)
    suspend fun delete(product: Product) = productDao.deleteProduct(product)
}
