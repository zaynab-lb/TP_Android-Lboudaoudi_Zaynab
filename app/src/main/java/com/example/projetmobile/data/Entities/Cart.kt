package com.example.projetmobile.data.Entities

data class Cart(
    val userId: String = "",
    val items: MutableList<CartItem> = mutableListOf()
)