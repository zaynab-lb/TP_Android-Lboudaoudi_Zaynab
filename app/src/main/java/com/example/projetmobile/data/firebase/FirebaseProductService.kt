package com.example.projetmobile.data.firebase

import com.example.projetmobile.data.Entities.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseProductService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getProducts(): List<Product> {
        return try {
            firestore.collection("products")
                .get()
                .await()
                .toObjects(Product::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getProductById(id: String): Product? {
        return try {
            firestore.collection("products")
                .document(id)
                .get()
                .await()
                .toObject(Product::class.java)
        } catch (e: Exception) {
            null
        }
    }
}