package com.example.projetmobile.ui.user.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.nav.Routes
import com.example.projetmobile.ui.user.AuthViewModel

@Composable
fun AdminHomeScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenue Admin", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = {
                viewModel.logout()
                navController.navigate(Routes.Login) {
                    popUpTo(Routes.AdminHome) { inclusive = true }
                }
            },
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Se déconnecter", color = Color.White)
        }

        Button(
            onClick = { navController.navigate(Routes.UserInfo) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Profil")
        }

    }
}

