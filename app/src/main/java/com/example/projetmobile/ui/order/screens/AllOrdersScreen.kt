package com.example.projetmobile.ui.order.screens

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
import com.example.projetmobile.ui.menu.component.AppMenu
import com.example.projetmobile.ui.order.OrderViewModel
import com.example.projetmobile.ui.user.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllOrdersScreen(
    orderViewModel: OrderViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val orders by orderViewModel.orders.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()
    val statuses = listOf("En attente", "En cours", "Livrée", "Annulée")

    LaunchedEffect(Unit) {
        orderViewModel.loadAllOrders()
    }

    Scaffold(
        topBar = {
            Column {
                AppMenu(
                    navController = navController,
                    authViewModel = authViewModel,
                    modifier = Modifier.fillMaxWidth()
                )
                TopAppBar(title = { Text("Liste Commandes") })
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (orders.isEmpty()) {
                Text(
                    "Aucune commande trouvée.",
                    modifier = Modifier.padding(padding).padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders) { order ->
                        var expanded by remember { mutableStateOf(false) }

                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Commande ID : ${order.orderId}")
                                Text("Utilisateur ID : ${order.userId}")
                                Text("Client : ${order.userName}")
                                Text("Adresse : ${order.userAddress}")
                                Text("Total : ${order.totalPrice} DH")
                                Text("Date : ${order.date.toDate()}")
                                Text("Statut : ${order.status}")

                                Spacer(modifier = Modifier.height(8.dp))

                                TextButton(onClick = { expanded = true }) {
                                    Text("Changer le statut")
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    statuses.forEach { status ->
                                        DropdownMenuItem(
                                            text = { Text(status) },
                                            onClick = {
                                                expanded = false
                                                orderViewModel.updateOrderStatus(order.orderId, status)
                                            }
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