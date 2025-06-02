package com.example.projetmobile.data.Repository

import android.util.Log
import com.example.projetmobile.R
import com.example.projetmobile.data.Api.ProductApi
import com.example.projetmobile.data.Entities.Product
import jakarta.inject.Inject

class ProductRepository @Inject constructor(private val api : ProductApi)
{

    suspend fun getProducts(): List<Product> {
        // fetch data from a remote server
        val products = api.getProducts()
        Log.d("products repo", "size :" + products.size)
        return products
    }

    suspend fun getProductById(id: String): Product? {
        return api.getProducts().find { it.id == id }
    }
}