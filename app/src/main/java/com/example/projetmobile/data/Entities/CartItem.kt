package com.example.projetmobile.data.Entities

data class CartItem(
    val productId: String = "",
    val title: String = "",
    val price: Double = 0.0,
    var quantity: Int = 1,
    val imageUrl: String = ""
)