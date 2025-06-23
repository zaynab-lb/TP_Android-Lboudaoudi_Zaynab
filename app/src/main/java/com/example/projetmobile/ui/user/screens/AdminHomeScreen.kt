package com.example.projetmobile.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AdminHomeScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        // Bouton de déconnexion aligné à droite
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Déconnexion")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Contenu admin
        Text(
            text = "Espace Administrateur",
            style = MaterialTheme.typography.headlineMedium
        )

        // Ici vous pouvez ajouter le reste de votre interface admin
        // ...
    }
}