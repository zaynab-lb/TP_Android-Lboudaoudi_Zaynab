package com.example.projetmobile.ui.product.component

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.projetmobile.data.Entities.Product
import com.example.projetmobile.ui.cart.CartViewModel
import com.example.projetmobile.ui.menu.component.AppMenu
import com.example.projetmobile.ui.user.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun DetailsScreen(
    product: Product,
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val cartViewModel: CartViewModel = hiltViewModel()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var quantityToAdd by remember { mutableStateOf(1) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()  // ← scrollState pour scroll vertical

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE1F5FE),
                            Color(0xFFB3E5FC)
                        )
                    )
                )
        ) {
            // Menu en haut
            AppMenu(
                navController = navController,
                authViewModel = authViewModel,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 10.dp,
                    color = Color.White.copy(alpha = 0.95f)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 32.dp)
                            .verticalScroll(scrollState),  // ← activation scroll vertical ici
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
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color(0xFF0288D1)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = "Description",
                                        tint = Color(0xFF0288D1)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Description",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFF0288D1)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = product.productDescription ?: "Aucune description disponible",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AttachMoney,
                                        contentDescription = "Prix",
                                        tint = Color(0xFF0288D1)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Prix : ${product.productPrice} DH",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFF0288D1)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Inventory2,
                                        contentDescription = "Stock",
                                        tint = Color(0xFF0288D1)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))

                                    val stockText = when {
                                        product.productQuantity == 0 -> "Rupture de stock"
                                        product.productQuantity < 10 -> "Stock faible (${product.productQuantity})"
                                        else -> "En stock (${product.productQuantity})"
                                    }

                                    val stockColor = when {
                                        product.productQuantity == 0 -> MaterialTheme.colorScheme.error
                                        product.productQuantity < 10 -> MaterialTheme.colorScheme.error
                                        else -> MaterialTheme.colorScheme.primary
                                    }

                                    Text(
                                        text = stockText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = stockColor
                                    )
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        // Contrôle + / - modernisé
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                        ) {
                            IconButton(
                                onClick = { if (quantityToAdd > 1) quantityToAdd-- },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFF0288D1), shape = RoundedCornerShape(12.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Réduire",
                                    tint = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(24.dp))

                            Text(
                                text = quantityToAdd.toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color(0xFF0288D1)
                            )

                            Spacer(modifier = Modifier.width(24.dp))

                            IconButton(
                                onClick = { if (quantityToAdd < product.productQuantity) quantityToAdd++ },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFF0288D1), shape = RoundedCornerShape(12.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Augmenter",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                when {
                                    quantityToAdd <= 0 -> {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Veuillez saisir une quantité valide")
                                        }
                                    }
                                    quantityToAdd > product.productQuantity -> {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Quantité demandée dépasse le stock disponible !")
                                        }
                                    }
                                    else -> {
                                        cartViewModel.addToCart(userId, product, quantityToAdd)
                                        cartViewModel.loadCart(userId)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Produit ajouté au panier !")
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                        ) {
                            Text(text = "Ajouter au panier", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                        ) {
                            Text(text = "Retour à la liste", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
