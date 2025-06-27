package com.example.projetmobile.ui.menu.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projetmobile.data.Entities.User
import com.example.projetmobile.nav.Routes
import com.example.projetmobile.ui.user.AuthViewModel

@Composable
fun AppMenu(
    navController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUser { user = it }
    }

    user?.let {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nom de la boutique stylisé
            Text(
                text = "Perla",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = Color(0xFF6D4C41),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // Barre de menu avec icônes
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .shadow(2.dp, RoundedCornerShape(24.dp)),
                color = Color(0xFFE1F5FE),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavigationIconButton(
                        icon = Icons.Default.Home,
                        contentDescription = "Accueil",
                        onClick = {
                            navController.navigate(
                                if (it.role == "admin") Routes.AdminHome else Routes.ClientHome
                            )
                        }
                    )

                    if (it.role == "client") {
                        NavigationIconButton(
                            icon = Icons.Default.ShoppingCart,
                            contentDescription = "Panier",
                            onClick = { navController.navigate(Routes.CartScreen) }
                        )
                        NavigationIconButton(
                            icon = Icons.Default.List,
                            contentDescription = "Commandes",
                            onClick = { navController.navigate(Routes.OrdersScreen) }
                        )
                    }

                    if (it.role == "admin") {
                        NavigationIconButton(
                            icon = Icons.Default.Inventory,
                            contentDescription = "Produits",
                            onClick = { navController.navigate("allProducts") }
                        )
                        NavigationIconButton(
                            icon = Icons.Default.Receipt,
                            contentDescription = "Commandes",
                            onClick = { navController.navigate(Routes.AllOrders) }
                        )
                        NavigationIconButton(
                            icon = Icons.Default.Group,
                            contentDescription = "Utilisateurs",
                            onClick = { navController.navigate("userList") }
                        )
                    }

                    NavigationIconButton(
                        icon = Icons.Default.Person,
                        contentDescription = "Profil",
                        onClick = { navController.navigate(Routes.UserInfo) }
                    )

                    NavigationIconButton(
                        icon = Icons.Default.ExitToApp,
                        contentDescription = "Déconnexion",
                        tint = MaterialTheme.colorScheme.error,
                        onClick = {
                            authViewModel.logout()
                            navController.navigate(Routes.Login) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun NavigationIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    IconButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
