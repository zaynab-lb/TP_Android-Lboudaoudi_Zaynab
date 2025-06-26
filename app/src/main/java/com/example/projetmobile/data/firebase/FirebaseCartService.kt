package com.example.projetmobile.data.firebase


import android.util.Log
import com.example.projetmobile.data.Entities.Cart
import com.example.projetmobile.data.Entities.CartItem
import com.example.projetmobile.data.Entities.Order
import com.example.projetmobile.data.Entities.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseCartService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun addToCart(userId: String, product: Product, quantity: Int = 1) {
        val cartItem = CartItem(product, quantity)
        val cartRef = firestore.collection("carts").document(userId)
        val snapshot = cartRef.get().await()

        if (snapshot.exists()) {
            val cart = snapshot.toObject(Cart::class.java)
            val existingItem = cart?.items?.find { it.product.productID == product.productID }
            if (existingItem != null) {
                existingItem.quantity += quantity
            } else {
                cart?.items?.add(cartItem)
            }
            cartRef.set(cart ?: return)
        } else {
            cartRef.set(Cart(userId = userId, items = mutableListOf(cartItem)))
        }
    }

    suspend fun getCart(userId: String): List<CartItem> {
        return try {
            val snapshot = firestore.collection("carts").document(userId).get().await()
            snapshot.toObject(Cart::class.java)?.items?.map {
                CartItem(
                    product = it.product,
                    quantity = it.quantity
                )
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e("FirebaseCart", "Error getting cart", e)
            emptyList()
        }
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

    suspend fun createOrder(order: Order) {
        firestore.collection("orders")
            .add(order)
            .await()
    }
}