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
import com.example.projetmobile.nav.Routes
import com.example.projetmobile.ui.user.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserInfoScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUser {
            user = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Mes Informations", style = MaterialTheme.typography.headlineMedium)

        user?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Nom : ${it.nom}")
            Text("Prénom : ${it.prenom}")
            Text("Email : ${it.email}")
            Text("Âge : ${it.age}")
            Text("Rôle : ${it.role}")
        } ?: run {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        Button(
            onClick = { navController.navigate(Routes.EditProfile) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Modifier mes infos")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Retour")
        }
    }
}
