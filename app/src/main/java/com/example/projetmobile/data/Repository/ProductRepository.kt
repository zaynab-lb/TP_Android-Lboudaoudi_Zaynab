package com.example.projetmobile.data.Repository

import android.util.Log
import com.example.projetmobile.data.Entities.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepository @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getProducts(): List<Product> {
        return try {
            val snapshot = db.collection("products").get().await()
            snapshot.documents.mapNotNull { it.toObject(Product::class.java) }
        } catch (e: Exception) {
            Log.e("Firebase", "Erreur Firestore : ${e.message}")
            emptyList()
        }
    }

    suspend fun getProductById(id: String): Product? {
        return try {
            val snapshot = db.collection("products").get().await()
            val products = snapshot.toObjects(Product::class.java)
            products.find { it.productID == id }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateProduct(product: Product) {
        db.collection("products")
            .document(product.productID)
            .set(product)
            .await()
    }

    suspend fun deleteProduct(productId: String) {
        db.collection("products")
            .document(productId)
            .delete()
            .await()
    }


}
