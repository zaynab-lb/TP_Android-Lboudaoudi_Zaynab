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
}