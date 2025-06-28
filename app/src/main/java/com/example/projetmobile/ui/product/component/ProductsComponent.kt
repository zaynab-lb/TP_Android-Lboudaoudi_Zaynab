package com.example.projetmobile.ui.product.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projetmobile.data.Entities.Product

@Composable
fun ProductsList(products: List<Product>, onNavigateToDetails: (String) -> Unit) {
    val filteredProducts = products.filter { it.productQuantity > 0 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE1F5FE),
                        Color(0xFFB3E5FC)
                    )
                )
            )
            .padding(16.dp)
    ) {
        LazyColumn {
            items(filteredProducts) { product ->
                ProductItem(
                    product = product,
                    onDetailsClick = { onNavigateToDetails(product.productID) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
