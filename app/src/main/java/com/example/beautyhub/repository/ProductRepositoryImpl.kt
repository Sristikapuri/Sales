package com.example.beautyhub.repository

import android.content.Context
import android.net.Uri
import com.example.beautyhub.model.ProductModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl : ProductRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override suspend fun getAllProducts(): Result<List<ProductModel>> = try {
        val snapshot = firestore.collection("products").get().await()
        val products = snapshot.documents.mapNotNull {
            it.toObject(ProductModel::class.java)?.copy(productID = it.id)
        }
        Result.success(products)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProductById(id: String): Result<ProductModel?> = try {
        val doc = firestore.collection("products").document(id).get().await()
        Result.success(doc.toObject(ProductModel::class.java)?.copy(productID = doc.id))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addProduct(product: ProductModel): Result<Unit> = try {
        firestore.collection("products").add(product).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateProduct(product: ProductModel): Result<Unit> = try {
        firestore.collection("products").document(product.productID).set(product).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteProduct(id: String): Result<Unit> = try {
        firestore.collection("products").document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun uploadImage(context: Context, uri: Uri): Result<String> = try {
        val path = "product_images/${System.currentTimeMillis()}.jpg"
        val ref = storage.reference.child(path)
        ref.putFile(uri).await()
        val url = ref.downloadUrl.await().toString()
        Result.success(url)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
