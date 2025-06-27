package com.example.projetmobile.ui.order.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
            }
        },
        containerColor = Color(0xFFE1F5FE)
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF0288D1))
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

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                OrderRow(icon = Icons.Default.Receipt, label = "Commande ID", value = order.orderId)
                                OrderRow(icon = Icons.Default.Person, label = "Utilisateur ID", value = order.userId)
                                OrderRow(icon = Icons.Default.AccountCircle, label = "Client", value = order.userName)
                                OrderRow(icon = Icons.Default.LocationOn, label = "Adresse", value = order.userAddress)
                                OrderRow(icon = Icons.Default.AttachMoney, label = "Total", value = "${order.totalPrice} DH")
                                OrderRow(icon = Icons.Default.DateRange, label = "Date", value = order.date.toDate().toString())
                                OrderRow(icon = Icons.Default.Info, label = "Statut", value = order.status)

                                Spacer(modifier = Modifier.height(8.dp))

                                TextButton(
                                    onClick = { expanded = true },
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF0288D1))
                                ) {
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

@Composable
fun OrderRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF0288D1),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$label : ", style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
    }
}
