package com.example.shoppingproject.model

data class Product(
    val productId: String = "",
    val productName: String = "",
    val productImage: String = "",
    val productPrice: Float = 0.0f,
    val amount: Int = 0
)
