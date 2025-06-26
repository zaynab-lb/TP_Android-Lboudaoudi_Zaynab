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

    suspend fun getUserOrders(userId: String): List<Order> {
        return try {
            firestore.collection("orders")
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .toObjects(Order::class.java)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String) {
        try {
            firestore.collection("orders")
                .document(orderId)
                .update("status", newStatus)
                .await()
            Log.d("Firebase", "Statut de la commande mis à jour")
        } catch (e: Exception) {
            Log.e("Firebase", "Erreur lors de la mise à jour du statut", e)
            throw e
        }
    }

}