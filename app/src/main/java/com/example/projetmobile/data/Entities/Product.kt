package com.example.projetmobile.data.Entities


data class Product(
    val productID: String = "",
    val productTitle: String? = null,
    val productPrice: Double = 0.0,
    val productQuantity: Int = 0,
    val productCategory: String? = null,
    val productDescription: String? = null,
    val productImageRes: String = ""
)
