package com.example.projetmobile.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.data.Entities.User
import com.example.projetmobile.ui.menu.component.AppMenu
import com.example.projetmobile.ui.user.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var user by remember { mutableStateOf<User?>(null) }

    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    var newPassword by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUser {
            user = it
            nom = it?.nom ?: ""
            prenom = it?.prenom ?: ""
            age = it?.age?.toString() ?: ""
            isLoading = false
        }
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Modifier mon profil", style = MaterialTheme.typography.headlineMedium)

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF0288D1))
                }
            } else {
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

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nouveau mot de passe (facultatif)") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Mot de passe actuel *") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                    onClick = {
                        if (currentPassword.isBlank()) {
                            errorMessage = "Le mot de passe actuel est requis."
                            return@Button
                        }

                        val updatedUser = user?.copy(
                            nom = nom,
                            prenom = prenom,
                            age = age.toIntOrNull() ?: 0
                        )

                        if (updatedUser != null) {
                            authViewModel.updatePassword(
                                currentPassword,
                                if (newPassword.isNotBlank()) newPassword else null
                            ) { success ->
                                if (success) {
                                    authViewModel.updateUser(updatedUser) {
                                        navController.popBackStack()
                                    }
                                } else {
                                    errorMessage = "Erreur lors de la mise à jour du mot de passe"
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0288D1),
                        contentColor = Color.White
                    )
                ) {
                    Text("Enregistrer")
                }

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.White
                    )
                ) {
                    Text("Annuler")
                }

            }
        }
    }
}

