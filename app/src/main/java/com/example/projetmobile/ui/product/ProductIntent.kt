package com.example.projetmobile.ui.product

sealed class ProductIntent {
    object LoadProducts : ProductIntent()
    data class FilterByCategory(val category: String?) : ProductIntent()
}