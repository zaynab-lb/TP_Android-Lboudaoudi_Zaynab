package com.example.projetmobile.ui.order.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    Scaffold(
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC))
                    )
                )
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            AppMenu(navController = navController, authViewModel = authViewModel, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mes commandes",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF0288D1),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Text(
                        text = error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                orders.isEmpty() -> {
                    Text(
                        text = "Aucune commande trouvée",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
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
}
