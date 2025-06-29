package com.example.projetmobile.data.firebase

import android.util.Log
import com.example.projetmobile.data.Entities.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseOrderService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun createOrder(order: Order) {
        try {
            firestore.collection("orders")
                .add(order)
                .await()
            Log.d("Firebase", "Commande créée avec succès")
        } catch (e: Exception) {
            Log.e("Firebase", "Erreur création commande", e)
            throw e
        }
    }

    suspend fun getAllOrders(): List<Order> {
        return try {
            val snapshot = firestore.collection("orders")
                .get()
                .await()

            snapshot.documents.map { doc ->
                val order = doc.toObject(Order::class.java)
                order?.copy(orderId = doc.id) ?: Order(orderId = doc.id)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUserOrders(userId: String): List<Order> {
        return try {
            val snapshot = firestore.collection("orders")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            snapshot.documents.map { doc ->
                val order = doc.toObject(Order::class.java)
                order?.copy(orderId = doc.id) ?: Order(orderId = doc.id)
            }
        } catch (e: Exception) {
            throw e
        }
    }


    suspend fun updateOrderStatus(orderId: String, newStatus: String): Order {
        try {
            firestore.collection("orders")
                .document(orderId)
                .update("status", newStatus)
                .await()

            // Récupérer l'ordre mis à jour
            val updatedDoc = firestore.collection("orders")
                .document(orderId)
                .get()
                .await()

            return updatedDoc.toObject(Order::class.java)?.copy(orderId = orderId)
                ?: throw Exception("Order not found")
        } catch (e: Exception) {
            throw e
        }
    }



}