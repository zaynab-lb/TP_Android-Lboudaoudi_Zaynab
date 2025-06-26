package com.example.projetmobile.ui.order.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.ui.menu.component.AppMenu
import com.example.projetmobile.ui.order.OrderViewModel
import com.example.projetmobile.ui.order.component.OrderItem
import com.example.projetmobile.ui.user.AuthViewModel

@Composable
fun OrdersScreen(
    orderViewModel: OrderViewModel,
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val orders by orderViewModel.orders.collectAsState()
    val isLoading by orderViewModel.isLoading.collectAsState()
    val error by orderViewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppMenu(navController = navController, authViewModel = authViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mes commandes",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            error != null -> {
                Text(text = error!!, color = MaterialTheme.colorScheme.error)
            }
            orders.isEmpty() -> {
                Text("Aucune commande trouvée")
            }
            else -> {
                LazyColumn {
                    items(orders) { order ->
                        OrderItem(order = order)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}