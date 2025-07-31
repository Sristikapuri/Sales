package com.example.beautyhub.model

data class ProductModel(
    val productID: String = "",
    val productName: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val image: String = "",
    val category: String = "",
    val dateAdded: Long = 0L
)
