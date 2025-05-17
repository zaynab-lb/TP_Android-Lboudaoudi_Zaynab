package com.example.projetmobile.ui.product.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projetmobile.data.Entities.Product

@Composable
fun ProductsList(products: List<Product>, onNavigateToDetails: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Liste des Produits",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(products) { product ->
                ProductItem(
                    product = product,
                    onDetailsClick = { onNavigateToDetails(product.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}