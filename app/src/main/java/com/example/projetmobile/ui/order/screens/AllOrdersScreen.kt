package com.example.projetmobile.ui.order.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.ui.menu.component.AppMenu
import com.example.projetmobile.ui.order.OrderViewModel
import com.example.projetmobile.ui.user.AuthViewModel
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        orderViewModel.loadAllOrders()
    }

    Scaffold(
        topBar = {
            Column {
                AppMenu(navController = navController, authViewModel = authViewModel, modifier = Modifier.fillMaxWidth())
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
                Text("Aucune commande trouvée.", modifier = Modifier.padding(padding).padding(16.dp))
            } else {
                // Gérer l'état d'ouverture par commande avec un map (id -> expanded)
                val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders) { order ->
                        val expanded = expandedStates.getOrElse(order.orderId) { false }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                OrderRow(icon = Icons.Default.Receipt, value = order.orderId)
                                OrderRow(icon = Icons.Default.Person, value = order.userId)
                                OrderRow(icon = Icons.Default.AccountCircle, value = order.userName)
                                OrderRow(icon = Icons.Default.LocationOn, value = order.userAddress)
                                OrderRow(icon = Icons.Default.AttachMoney, value = "${order.totalPrice} DH")
                                OrderRow(icon = Icons.Default.DateRange, value = order.date.toDate().toString())

                                Spacer(modifier = Modifier.height(8.dp))

                                OrderStatus(status = order.status)

                                Spacer(modifier = Modifier.height(8.dp))

                                TextButton(
                                    onClick = {
                                        expandedStates[order.orderId] = !expanded
                                    },
                                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF0288D1))
                                ) {
                                    Text("Changer le statut")
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expandedStates[order.orderId] = false },
                                    containerColor = Color(0xFFBBDEFB) // fond bleu clair
                                ) {
                                    statuses.forEach { status ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = status,
                                                    color = if (status == order.status) Color(0xFF0D47A1) else Color(0xFF1565C0), // texte bleu foncé
                                                    style = if (status == order.status) MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                                    ) else MaterialTheme.typography.bodyMedium
                                                )
                                            },
                                            onClick = {
                                                expandedStates[order.orderId] = false
                                                coroutineScope.launch {
                                                    orderViewModel.updateOrderStatus(order.orderId, status)
                                                    orderViewModel.loadAllOrders()
                                                }
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
fun OrderRow(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF0288D1),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun OrderStatus(status: String) {
    val (icon, color, label) = when (status) {
        "En attente" -> Triple(Icons.Default.Info, Color(0xFFFFA000), "En attente") // orange
        "En cours" -> Triple(Icons.Default.Info, Color(0xFF0288D1), "En cours") // bleu
        "Livrée" -> Triple(Icons.Default.CheckCircle, Color(0xFF388E3C), "Livrée") // vert
        "Annulée" -> Triple(Icons.Default.Cancel, Color(0xFFD32F2F), "Annulée") // rouge
        else -> Triple(Icons.Default.Help, Color.Gray, status)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}
