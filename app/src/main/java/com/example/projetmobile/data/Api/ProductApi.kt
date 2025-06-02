package com.example.projetmobile.data.Api

import com.example.projetmobile.data.Entities.Product
import retrofit2.http.GET

interface ProductApi {
    @GET("product.json")
    suspend fun getProducts(): List<Product>
}