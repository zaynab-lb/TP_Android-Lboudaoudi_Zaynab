package com.example.projetmobile.ui.cart.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.nav.Routes
import com.example.projetmobile.ui.cart.CartViewModel
import com.example.projetmobile.ui.menu.component.AppMenu
import com.example.projetmobile.ui.user.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Scaffold(
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC))
                    )
                )
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Menu haut
            AppMenu(navController = navController, authViewModel = authViewModel, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mon Panier",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF0288D1)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (cartItems.isEmpty()) {
                Text(
                    text = "Votre panier est vide.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(cartItems) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = item.product.productTitle ?: "",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF0288D1)
                                )
                                Text(text = "Prix unitaire : ${item.product.productPrice} DH")
                                Text(text = "Total : ${item.quantity * item.product.productPrice} DH")

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Button(
                                        onClick = {
                                            if (item.quantity > 1)
                                                cartViewModel.updateQuantity(
                                                    userId,
                                                    item.product.productID,
                                                    item.quantity - 1
                                                )
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.size(36.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text("-", style = MaterialTheme.typography.titleLarge)
                                    }

                                    Text(
                                        text = "${item.quantity}",
                                        modifier = Modifier.padding(horizontal = 12.dp),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFF0288D1)
                                    )

                                    Button(
                                        onClick = {
                                            if (item.quantity < item.product.productQuantity)
                                                cartViewModel.updateQuantity(
                                                    userId,
                                                    item.product.productID,
                                                    item.quantity + 1
                                                )
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.size(36.dp),
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text("+", style = MaterialTheme.typography.titleLarge)
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    OutlinedButton(
                                        onClick = {
                                            cartViewModel.removeItem(userId, item.product.productID)
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Supprimer")
                                        Spacer(modifier = Modifier.width(4.dp))
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
                    color = Color(0xFF0288D1),
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
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                ) {
                    Text("Continuer les achats", color = Color.White)
                }

                if (cartItems.isNotEmpty()) {
                    Button(
                        onClick = { cartViewModel.clearCart(userId) },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Vider le panier", color = MaterialTheme.colorScheme.onError)
                    }
                }
            }

            if (cartItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { navController.navigate(Routes.Checkout) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                ) {
                    Text("Valider la commande", color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp)) // évite d'être collé à la barre système
            }
        }
    }
}
