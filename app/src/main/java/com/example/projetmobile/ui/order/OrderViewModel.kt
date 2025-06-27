package com.example.projetmobile.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetmobile.data.Entities.Order
import com.example.projetmobile.data.firebase.FirebaseOrderService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderService: FirebaseOrderService
) : ViewModel() {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Charge commandes d’un utilisateur (client)
    fun loadUserOrders(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userOrders = orderService.getUserOrders(userId)
                _orders.value = userOrders
            } catch (e: Exception) {
                _error.value = "Erreur lors du chargement des commandes"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Charge toutes les commandes (admin)
    fun loadAllOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allOrders = orderService.getAllOrders()
                _orders.value = allOrders
            } catch (e: Exception) {
                _error.value = "Erreur lors du chargement des commandes"
            } finally {
                _isLoading.value = false
            }
        }
    }

}