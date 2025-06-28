package com.example.projetmobile.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.nav.Routes
import com.example.projetmobile.ui.menu.component.AppMenu
import com.example.projetmobile.ui.order.OrderViewModel
import com.example.projetmobile.ui.product.ProductViewModel
import com.example.projetmobile.ui.user.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
    productViewModel: ProductViewModel = hiltViewModel()
) {
    // Observe counts from respective viewmodels
    val clientCount by authViewModel.clientCount.collectAsState(initial = 0)
    val orderCount by orderViewModel.orderCount.collectAsState(initial = 0)
    val productCount by productViewModel.productCount.collectAsState(initial = 0)

    // Load data on composition
    LaunchedEffect(Unit) {
        authViewModel.loadAllUsers { users ->
            authViewModel._clientCount.value = users.count { it.role == "client" }
        }
        orderViewModel.loadAllOrders()
        productViewModel.handleIntent(com.example.projetmobile.ui.product.ProductIntent.LoadProducts)
    }

    Scaffold(
        topBar = {
            AppMenu(navController = navController, authViewModel = authViewModel)
        },
        containerColor = Color(0xFFE1F5FE)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Bienvenue Admin",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF0288D1)
            )

            // Row affichant les compteurs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CountCard("Clients", clientCount, Icons.Default.People, Color(0xFF9C27B0), Modifier.weight(1f))
                CountCard("Commandes", orderCount, Icons.Default.ShoppingCart, Color(0xFF4CAF50), Modifier.weight(1f))
                CountCard("Produits", productCount, Icons.Default.List, Color(0xFF0288D1), Modifier.weight(1f))
            }

            // Actions admin
            AdminActionCard(
                title = "Liste Produits",
                icon = Icons.Default.List,
                color = Color(0xFF0288D1)
            ) {
                navController.navigate("allProducts")
            }

            AdminActionCard(
                title = "Liste Commandes",
                icon = Icons.Default.ShoppingCart,
                color = Color(0xFF4CAF50)
            ) {
                navController.navigate(Routes.AllOrders)
            }

            AdminActionCard(
                title = "Liste Utilisateurs",
                icon = Icons.Default.People,
                color = Color(0xFF9C27B0)
            ) {
                navController.navigate("userList")
            }
        }
    }
}

@Composable
fun CountCard(
    title: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(count.toString(), style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Text(title, style = MaterialTheme.typography.bodyMedium, color = Color.White)
        }
    }
}

@Composable
fun AdminActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Color.White, style = MaterialTheme.typography.titleMedium)
            Icon(icon, contentDescription = title, tint = Color.White)
        }
    }
}
