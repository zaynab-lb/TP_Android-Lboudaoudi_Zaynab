package com.example.projetmobile.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.data.Entities.User
import com.example.projetmobile.ui.user.AuthViewModel

@Composable
fun EditProfileScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {
    var user by remember { mutableStateOf<User?>(null) }

    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUser {
            user = it
            nom = it?.nom ?: ""
            prenom = it?.prenom ?: ""
            age = it?.age?.toString() ?: ""
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Modifier mon profil", style = MaterialTheme.typography.headlineMedium)

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nom,
                onValueChange = { nom = it },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = prenom,
                onValueChange = { prenom = it },
                label = { Text("Prénom") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = age,
                onValueChange = { age = it.filter { char -> char.isDigit() } },
                label = { Text("Âge") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val updatedUser = user?.copy(
                        nom = nom,
                        prenom = prenom,
                        age = age.toIntOrNull() ?: 0
                    )
                    if (updatedUser != null) {
                        authViewModel.updateUser(updatedUser) {
                            navController.popBackStack() // Retour à l’écran précédent
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enregistrer")
            }
        }
    }
}
