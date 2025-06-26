package com.example.projetmobile.data.Entities

data class CartItem(
    val product: Product = Product(),
    var quantity: Int = 1,
)