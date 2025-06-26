package com.example.projetmobile.ui.cart.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.nav.Routes
import com.example.projetmobile.ui.cart.CartViewModel
import com.example.projetmobile.ui.menu.component.AppMenu
import com.example.projetmobile.ui.user.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CartScreen(cartViewModel: CartViewModel, navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppMenu(navController = navController, authViewModel = authViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Mon Panier", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Text("Votre panier est vide.")
            Spacer(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = item.product.productTitle ?: "",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(text = "Prix unitaire : ${item.product.productPrice} DH")
                            Text(text = "Total : ${item.quantity * item.product.productPrice} DH")

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(onClick = {
                                    if (item.quantity > 1)
                                        cartViewModel.updateQuantity(
                                            userId,
                                            item.product.productID,
                                            item.quantity - 1
                                        )
                                }) {
                                    Text("-")
                                }

                                Text(
                                    text = "${item.quantity}",
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )

                                Button(onClick = {
                                    if (item.quantity < item.product.productQuantity)
                                        cartViewModel.updateQuantity(
                                            userId,
                                            item.product.productID,
                                            item.quantity + 1
                                        )
                                }) {
                                    Text("+")
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                OutlinedButton(onClick = {
                                    cartViewModel.removeItem(userId, item.product.productID)
                                }) {
                                    Text("Supprimer")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val totalPrice = cartItems.sumOf { it.quantity * it.product.productPrice }

            Text(
                text = "Total : $totalPrice DH",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

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
                    onClick = { cartViewModel.clearCart(userId) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Vider le panier", color = MaterialTheme.colorScheme.onError)
                }
            }
        }

        if (cartItems.isNotEmpty()) {
            Button(
                onClick = { navController.navigate(Routes.Checkout) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Valider la commande")
            }
        }
    }
}