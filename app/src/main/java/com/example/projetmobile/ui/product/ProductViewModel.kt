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

    private var allProducts: List<Product> = emptyList()

    fun handleIntent(intent: ProductIntent) {
        when (intent) {
            is ProductIntent.LoadProducts -> {
                viewModelScope.launch {
                    loadProducts()
                }
            }
            is ProductIntent.FilterByCategory -> {
                filterByCategory(intent.category)
            }
        }
    }

    private suspend fun loadProducts() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            allProducts = repository.getProducts()
            _state.value = ProductViewState(
                isLoading = false,
                products = allProducts,
                categories = allProducts.map { it.productCategory }.distinct().filterNotNull()
            )
        } catch (e: Exception) {
            _state.value =
                ProductViewState(isLoading = false, error = e.message ?: "Error fetching products")
        }
    }

    private fun filterByCategory(category: String?) {
        _state.value = if (category == null) {
            _state.value.copy(products = allProducts, selectedCategory = null)
        } else {
            _state.value.copy(
                products = allProducts.filter { it.productCategory == category },
                selectedCategory = category
            )
        }
    }

    fun getProductById(productId: String, onResult: (Product?) -> Unit) {
        viewModelScope.launch {
            val product = repository.getProductById(productId)
            onResult(product)
        }
    }

    fun getAllProducts(onResult: (List<Product>) -> Unit) {
        viewModelScope.launch {
            val products = repository.getProducts()
            onResult(products)
        }
    }

    fun getProducts(callback: (List<Product>) -> Unit) {
        viewModelScope.launch {
            val result = repository.getProducts()
            callback(result)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }



}