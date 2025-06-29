package com.example.projetmobile.ui.cart.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
                                    modifier = Modifier.fillMaxWidth()
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

                                    Spacer(modifier = Modifier.weight(1f)) // Pousse le bouton à droite

                                    IconButton(
                                        onClick = {
                                            cartViewModel.removeItem(userId, item.product.productID)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Supprimer",
                                            tint = MaterialTheme.colorScheme.error
                                        )
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        onClick = { cartViewModel.clearCart(userId) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.error),
                        // Pas de shape pour IconButton, mais on peut wrapper si besoin
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Vider le panier",
                            tint = MaterialTheme.colorScheme.onError,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    IconButton(
                        onClick = { navController.navigate(Routes.Checkout) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF0288D1))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Valider la commande",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}
