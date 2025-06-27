package com.example.projetmobile.ui.product.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.projetmobile.data.Entities.Product
import com.example.projetmobile.ui.product.ProductViewModel
import com.example.projetmobile.ui.product.component.ProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProductsScreen(
    productViewModel: ProductViewModel,
    navController: NavController
) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        productViewModel.getProducts { result ->
            products = result
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tous les Produits") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products) { product ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                // Image du produit
                                Image(
                                    painter = rememberAsyncImagePainter(model = product.productImageRes),
                                    contentDescription = product.productTitle,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(end = 16.dp)
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = product.productTitle ?: "Sans titre",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Catégorie : ${product.productCategory ?: "Non spécifiée"}",
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    Text(
                                        text = "Prix : ${product.productPrice} DH",
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    Text(
                                        text = when {
                                            product.productQuantity == 0 -> "Rupture de stock"
                                            product.productQuantity < 10 -> "Stock faible (${product.productQuantity})"
                                            else -> "Stock : ${product.productQuantity}"
                                        },
                                        color = when {
                                            product.productQuantity == 0 -> MaterialTheme.colorScheme.error
                                            product.productQuantity < 10 -> MaterialTheme.colorScheme.tertiary
                                            else -> MaterialTheme.colorScheme.primary
                                        },
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
