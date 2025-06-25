package com.example.projetmobile.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetmobile.data.Entities.CartItem
import com.example.projetmobile.data.firebase.FirebaseCartService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartService: FirebaseCartService
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun loadCart(userId: String) {
        viewModelScope.launch {
            _cartItems.value = cartService.getCart(userId)
        }
    }

    fun addToCart(userId: String, item: CartItem) {
        viewModelScope.launch {
            cartService.addToCart(userId, item)
            loadCart(userId) // Refresh cart
        }
    }




    fun removeItem(userId: String, productId: String) {
        viewModelScope.launch {
            val updatedList = _cartItems.value.filterNot { it.productId == productId }
            cartService.updateCart(userId, updatedList)
            _cartItems.value = updatedList
        }
    }

    fun updateQuantity(userId: String, productId: String, newQuantity: Int) {
        viewModelScope.launch {
            val updatedList = _cartItems.value.map {
                if (it.productId == productId) it.copy(quantity = newQuantity) else it
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

}
