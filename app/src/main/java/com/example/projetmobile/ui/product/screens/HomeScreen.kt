package com.example.projetmobile.ui.product.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetmobile.ui.product.ProductViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.graphics.Color
import com.example.projetmobile.ui.product.ProductIntent
import com.example.projetmobile.ui.product.component.ProductsList

@Composable
fun HomeScreen(
    viewModel: ProductViewModel = viewModel(),
    onNavigateToDetails: (String) -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        // Call loadProducts() method when the composable is first launched.
        viewModel.handleIntent(ProductIntent.LoadProducts)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {

        // Ajout de la Row pour le bouton de déconnexion
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Déconnexion")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtres par catégorie
        CategoryFilter(
            categories = state.categories,
            selectedCategory = state.selectedCategory,
            onCategorySelected = { category ->
                viewModel.handleIntent(ProductIntent.FilterByCategory(category))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading -> {
                // Display a Circular loader
                CircularProgressIndicator(modifier = Modifier.align(CenterHorizontally))
            }

            state.error != null -> {
                // Display an error message
                Text(text = "Error: ${state.error}", color = Color.Red)
            }

            else -> {
                // Display products when fetch is success
                ProductsList(products = state.products, onNavigateToDetails)
            }
        }
    }
}

@Composable
fun CategoryFilter(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            item {
                Row {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { onCategorySelected(null) },
                        label = { Text("Tous") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { onCategorySelected(category) },
                            label = { Text(category) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}