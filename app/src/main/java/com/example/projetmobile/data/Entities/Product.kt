package com.example.projetmobile.data.Entities

import android.R
import androidx.annotation.DrawableRes
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
    @SerializedName("productImageRes")
    val imageRes: Int
)