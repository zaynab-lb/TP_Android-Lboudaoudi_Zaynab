package com.example.projetmobile.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.data.Entities.User
import com.example.projetmobile.ui.user.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var selectedRole by remember { mutableStateOf("all") }

    LaunchedEffect(Unit) {
        viewModel.loadAllUsers { fetchedUsers ->
            users = fetchedUsers
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Liste des Utilisateurs") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Filtrer par rôle :", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { selectedRole = "all" }) { Text("Tous") }
                Button(onClick = { selectedRole = "client" }) { Text("Clients") }
                Button(onClick = { selectedRole = "admin" }) { Text("Admins") }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                val filteredUsers = users.filter {
                    selectedRole == "all" || it.role.equals(selectedRole, ignoreCase = true)
                }

                if (filteredUsers.isEmpty()) {
                    item {
                        Text("Aucun utilisateur trouvé pour ce filtre.")
                    }
                } else {
                    items(filteredUsers) { user ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Nom : ${user.nom} ${user.prenom}")
                                Text("Email : ${user.email}")
                                Text("Rôle : ${user.role}")
                            }
                        }
                    }
                }
            }
        }
    }
}
