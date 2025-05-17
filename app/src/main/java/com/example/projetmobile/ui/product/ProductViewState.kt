package com.example.projetmobile.ui.product

import com.example.projetmobile.data.Entities.Product

data class ProductViewState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)