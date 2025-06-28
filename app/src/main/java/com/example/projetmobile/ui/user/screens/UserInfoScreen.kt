package com.example.projetmobile.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.data.Entities.User
import com.example.projetmobile.nav.Routes
import com.example.projetmobile.ui.menu.component.AppMenu
import com.example.projetmobile.ui.user.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUser {
            user = it
        }
    }

    Scaffold(
        topBar = {
            AppMenu(navController = navController, authViewModel = authViewModel)
        },
        containerColor = Color(0xFFE1F5FE)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            user?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Mes Informations", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF0288D1))

                        InfoRow(label = "Nom", value = it.nom)
                        InfoRow(label = "Prénom", value = it.prenom)
                        InfoRow(label = "Email", value = it.email)
                        InfoRow(label = "Âge", value = it.age.toString())
                        InfoRow(label = "Rôle", value = it.role)

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.navigate(Routes.EditProfile) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0288D1),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Modifier mes infos")
                        }
                    }
                }
            } ?: CircularProgressIndicator(color = Color(0xFF0288D1))
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "$label :", style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
    }
}
