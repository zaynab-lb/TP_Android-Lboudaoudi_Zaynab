package com.example.projetmobile.ui.cart.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projetmobile.nav.Routes
import com.example.projetmobile.ui.cart.CartViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CartScreen(cartViewModel: CartViewModel, navController: NavController) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mon Panier", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Text("Votre panier est vide.")
            Spacer(modifier = Modifier.weight(1f)) // Pousse les boutons en bas
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Prix unitaire : ${item.price} DH")
                            Text(text = "Total : ${item.quantity * item.price} DH")

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(onClick = {
                                    if (item.quantity > 1)
                                        cartViewModel.updateQuantity(userId, item.productId, item.quantity - 1)
                                }) {
                                    Text("-")
                                }

                                Text(
                                    text = "${item.quantity}",
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )

                                Button(onClick = {
                                    val maxStock = 10
                                    if (item.quantity < maxStock)
                                        cartViewModel.updateQuantity(userId, item.productId, item.quantity + 1)
                                }) {
                                    Text("+")
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                OutlinedButton(onClick = {
                                    cartViewModel.removeItem(userId, item.productId)
                                }) {
                                    Text("Supprimer")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val totalPrice = cartItems.sumOf { it.quantity * it.price }

            Text(
                text = "Total : $totalPrice DH",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Boutons affichés dans tous les cas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    navController.navigate(Routes.ClientHome) {
                        popUpTo(Routes.CartScreen) { inclusive = true }
                    }
                }
            ) {
                Text("Continuer les achats")
            }

            if (cartItems.isNotEmpty()) {
                Button(
                    onClick = {
                        cartViewModel.clearCart(userId)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Vider le panier", color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    }
}
