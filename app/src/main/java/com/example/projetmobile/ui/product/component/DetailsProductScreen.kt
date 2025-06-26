package com.example.projetmobile.ui.product.component

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.projetmobile.data.Entities.CartItem
import com.example.projetmobile.data.Entities.Product
import com.example.projetmobile.ui.cart.CartViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun DetailsScreen(product: Product, navController: NavController) {
    val cartViewModel: CartViewModel = hiltViewModel()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var quantityToAdd by remember { mutableStateOf("1") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = product.productImageRes),
                contentDescription = product.productTitle,
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = product.productTitle.orEmpty(),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Catégorie: ${product.productCategory}")
            Text("Prix: ${product.productPrice} DH")
            Text("Stock disponible: ${product.productQuantity}")

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = quantityToAdd,
                onValueChange = { quantityToAdd = it },
                label = { Text("Quantité") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val qty = quantityToAdd.toIntOrNull()
                    when {
                        qty == null || qty <= 0 -> {
                            scope.launch {
                                snackbarHostState.showSnackbar("Veuillez saisir une quantité valide")
                            }
                        }
                        qty > product.productQuantity -> {
                            scope.launch {
                                snackbarHostState.showSnackbar("Quantité demandée dépasse le stock disponible !")
                            }
                        }
                        else -> {
                            cartViewModel.addToCart(userId, product, qty)
                            cartViewModel.loadCart(userId)
                            scope.launch {
                                snackbarHostState.showSnackbar("Produit ajouté au panier !")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Ajouter au panier")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Retour à la liste")
            }
        }
    }
}