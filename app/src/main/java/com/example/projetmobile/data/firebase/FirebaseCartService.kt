package com.example.projetmobile.data.firebase


import com.example.projetmobile.data.Entities.Cart
import com.example.projetmobile.data.Entities.CartItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseCartService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun addToCart(userId: String, item: CartItem) {
        val cartRef = firestore.collection("carts").document(userId)
        val snapshot = cartRef.get().await()

        if (snapshot.exists()) {
            val cart = snapshot.toObject(Cart::class.java)
            val existingItem = cart?.items?.find { it.productId == item.productId }
            if (existingItem != null) {
                existingItem.quantity += item.quantity
            } else {
                cart?.items?.add(item)
            }
            cartRef.set(cart ?: return)
        } else {
            cartRef.set(mapOf("userId" to userId, "items" to listOf(item)))
        }
    }

    suspend fun getCart(userId: String): List<CartItem> {
        val snapshot = firestore.collection("carts").document(userId).get().await()
        return snapshot.toObject(Cart::class.java)?.items ?: emptyList()
    }



    suspend fun updateCart(userId: String, items: List<CartItem>) {
        firestore.collection("carts").document(userId)
            .update("items", items)
            .await()
    }

    suspend fun clearCart(userId: String) {
        firestore.collection("carts").document(userId)
            .update("items", emptyList<CartItem>())
            .await()
    }

}
