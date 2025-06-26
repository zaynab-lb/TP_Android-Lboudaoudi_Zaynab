package com.example.projetmobile.data.Entities

import com.google.firebase.Timestamp

data class Order(
    val userId: String = "",
    val userName: String = "",
    val userAddress: String = "",
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val date: Timestamp = Timestamp.now()
)