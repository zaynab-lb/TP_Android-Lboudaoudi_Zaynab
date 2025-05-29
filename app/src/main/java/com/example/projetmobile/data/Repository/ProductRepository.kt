package com.example.projetmobile.data.Repository

import android.util.Log
import com.example.projetmobile.R
import com.example.projetmobile.data.Api.ProductApi
import com.example.projetmobile.data.Entities.Product
import jakarta.inject.Inject

class ProductRepository @Inject constructor(private val api : ProductApi)
{
    /*private val products = listOf(
        Product("1", "Clavier", 150.00, 0, R.drawable.clavier),
        Product("2", "Souris", 75.00, 5, R.drawable.souris),
        Product("3", "Ecran", 7500.00, 20, R.drawable.ecran),
        Product("4", "PC Portable", 6999.99, 50, R.drawable.pc),
        Product("5", "Tapis", 59.00, 200, R.drawable.tapis),
    )

    fun getProducts(): List<Product> {
        return products
    }

    fun getProductById(id: String): Product? {
        return products.find { it.id == id }
    }*/

    suspend fun getProducts(): List<Product> {
        // fetch data from a remote server
        val products = api.getProducts()
        Log.d("products repo", "size :" + products.size)
        return products
    }

}