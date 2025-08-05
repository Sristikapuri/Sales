package com.example.beautyhub.repository


import android.content.Context
import android.net.Uri
import com.example.beautyhub.model.ProductModel


interface ProductRepository {
    // Product CRUD operations
    fun addProduct(model: ProductModel, callback: (Boolean, String) -> Unit)
    fun deleteProduct(productID: String, callback: (Boolean, String) -> Unit)
    fun updateProduct(productID: String, productData: MutableMap<String, Any?>, callback: (Boolean, String) -> Unit)
    fun getProductByID(productID: String, callback: (ProductModel?, Boolean, String) -> Unit)
    fun getAllProduct(callback: (List<ProductModel?>, Boolean, String) -> Unit)

    // Image operations
    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit)
    fun getFileNameFromUri(context: Context, uri: Uri): String?
}