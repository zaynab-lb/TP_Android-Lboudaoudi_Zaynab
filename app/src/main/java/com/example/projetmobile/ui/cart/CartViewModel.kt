package com.example.projetmobile.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetmobile.data.Entities.CartItem
import com.example.projetmobile.data.Entities.Order
import com.example.projetmobile.data.Entities.Product
import com.example.projetmobile.data.firebase.FirebaseCartService
import com.example.projetmobile.data.firebase.FirebaseOrderService
import com.example.projetmobile.data.firebase.FirebaseProductService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartService: FirebaseCartService,
    private val productService: FirebaseProductService,
    private val orderService: FirebaseOrderService
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    fun loadCart(userId: String) {
        viewModelScope.launch {
            try {
                val items = cartService.getCart(userId)
                Log.d("CartDebug", "Loaded ${items.size} items")
                items.forEach {
                    Log.d("CartDebug", "Item: ${it.product.productTitle} Qty: ${it.quantity}")
                }
                _cartItems.value = items
            } catch (e: Exception) {
                Log.e("CartError", "Failed to load cart", e)
            }
        }
    }
    fun addToCart(userId: String, product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            cartService.addToCart(userId, product, quantity)
            loadCart(userId) // Refresh cart
        }
    }

    fun removeItem(userId: String, productId: String) {
        viewModelScope.launch {
            val updatedList = _cartItems.value.filterNot { it.product.productID == productId }
            cartService.updateCart(userId, updatedList)
            _cartItems.value = updatedList
        }
    }

    fun updateQuantity(userId: String, productId: String, newQuantity: Int) {
        viewModelScope.launch {
            val updatedList = _cartItems.value.map {
                if (it.product.productID == productId) it.copy(quantity = newQuantity) else it
            }
            cartService.updateCart(userId, updatedList)
            _cartItems.value = updatedList
        }
    }

    fun clearCart(userId: String) {
        viewModelScope.launch {
            cartService.clearCart(userId)
            _cartItems.value = emptyList()
        }
    }

    fun validateOrder(
        userId: String,
        name: String,
        address: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                // 1. Recharger directement depuis Firestore pour avoir les données fraîches
                val currentCartItems = cartService.getCart(userId)

                if (currentCartItems.isEmpty()) {
                    throw Exception("Votre panier est vide")
                }

                // 2. Vérifier les stocks
                currentCartItems.forEach { item ->
                    val product = item.product
                    if (product.productQuantity < item.quantity) {
                        throw Exception("Stock insuffisant pour ${product.productTitle}")
                    }
                }

                // 3. Créer la commande
                val order = Order(
                    userId = userId,
                    userName = name,
                    userAddress = address,
                    items = currentCartItems,
                    totalPrice = currentCartItems.sumOf { it.product.productPrice * it.quantity }
                )

                orderService.createOrder(order)

                // 4. Mettre à jour les stocks
                currentCartItems.forEach { item ->
                    val newQuantity = item.product.productQuantity - item.quantity
                    productService.updateProductQuantity(item.product.productID, newQuantity)
                }

                // 5. Vider le panier
                cartService.clearCart(userId)
                _cartItems.value = emptyList()

                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Erreur: ${e.message}"
                Log.e("Order", "Erreur création commande", e)
            }
        }
    }
}