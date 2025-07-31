package com.example.beautyhub.repository

import android.content.Context
import android.net.Uri
import com.example.beautyhub.model.ProductModel

interface ProductRepository {
    suspend fun getAllProducts(): Result<List<ProductModel>>
    suspend fun getProductById(id: String): Result<ProductModel?>
    suspend fun addProduct(product: ProductModel): Result<Unit>
    suspend fun updateProduct(product: ProductModel): Result<Unit>
    suspend fun deleteProduct(id: String): Result<Unit>
    suspend fun uploadImage(context: Context, uri: Uri): Result<String>
}
