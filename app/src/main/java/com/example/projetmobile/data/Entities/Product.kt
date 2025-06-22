package com.example.projetmobile.data.Entities


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("productID")
    val id: String,
    @SerializedName("productTitle")
    val title: String? = null,
    @SerializedName("productPrice")
    val price: Double,
    @SerializedName("productQuantity")
    val quantity: Int,
    @SerializedName("productCategory")
    val category: String? = null,
    @SerializedName("productDescription")
    val description: String? = null,
    @SerializedName("productImageRes")
    val imageRes: String
)