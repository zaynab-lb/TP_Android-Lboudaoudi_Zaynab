package com.example.projetmobile.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetmobile.data.Entities.Product
import com.example.projetmobile.data.Repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import jakarta.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor( private val repository: ProductRepository) : ViewModel()
{
    private val _state = MutableStateFlow(ProductViewState())
    val state: StateFlow<ProductViewState> = _state
    //val repository: ProductRepository = ProductRepository()

    fun handleIntent(intent: ProductIntent) {
        when (intent) {
            is ProductIntent.LoadProducts -> {
                viewModelScope.launch {
                    loadProducts()
                }
            }
        }
    }

    private fun loadProducts() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val products = repository.getProducts()
            _state.value = ProductViewState(isLoading = false, products = products)
        } catch (e: Exception) {
            _state.value =
                ProductViewState(isLoading = false, error = e.message ?: "Error fetching products")
        }
    }


    fun getProductById(productId: String): Product? {
        return repository.getProductById(productId)
    }

    fun getAllProducts(): List<Product> {
        return repository.getProducts()
    }
}