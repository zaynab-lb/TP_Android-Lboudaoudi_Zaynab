package com.example.projetmobile.ui.product.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.projetmobile.data.Entities.Product
import com.example.projetmobile.ui.menu.component.AppMenu
import com.example.projetmobile.ui.product.ProductViewModel
import com.example.projetmobile.ui.user.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProductsScreen(
    productViewModel: ProductViewModel,
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    fun loadProducts() {
        coroutineScope.launch {
            productViewModel.getProducts { result ->
                products = result
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { loadProducts() }

    if (showDialog && productToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "Confirmation",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF0288D1))
                )
            },
            text = {
                Text(
                    "Voulez-vous vraiment supprimer ce produit ?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            productViewModel.deleteProduct(productToDelete!!.productID)
                            showDialog = false
                            productToDelete = null
                            loadProducts()
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Supprimer", style = MaterialTheme.typography.bodyLarge)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        productToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Annuler", style = MaterialTheme.typography.bodyLarge)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color(0xFFE1F5FE),
            tonalElevation = 8.dp
        )
    }

    Scaffold(
        topBar = {
            Column {
                AppMenu(
                    navController = navController,
                    authViewModel = authViewModel,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = Color(0xFFE1F5FE)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = Color(0xFF0288D1)
                )
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
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = product.productImageRes),
                                        contentDescription = product.productTitle,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .padding(end = 16.dp)
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            product.productTitle ?: "Sans titre",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = Color(0xFF0288D1)
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    "Catégorie : ${product.productCategory ?: "Non spécifiée"}",
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                                Text(
                                                    "Prix : ${product.productPrice} DH",
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
                                            }

                                            // Icônes à droite
                                            Row {
                                                IconButton(onClick = {
                                                    productToDelete = product
                                                    showDialog = true
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Supprimer",
                                                        tint = MaterialTheme.colorScheme.error
                                                    )
                                                }

                                                IconButton(onClick = {
                                                    navController.navigate("editProduct/${product.productID}")
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "Modifier",
                                                        tint = Color(0xFF0288D1)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
