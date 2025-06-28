package com.example.projetmobile.ui.user.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.data.Entities.User
import com.example.projetmobile.ui.menu.component.AppMenu
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
        viewModel.loadAllUsers { fetchedUsers -> users = fetchedUsers }
    }

    Scaffold(
        topBar = {
            Column {
                AppMenu(
                    navController = navController,
                    authViewModel = viewModel,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = Color(0xFFE1F5FE)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                listOf("Tous" to "all", "Clients" to "client", "Admins" to "admin").forEach { (label, role) ->
                    Button(
                        onClick = { selectedRole = role },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0288D1),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(label)
                    }
                }
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
                        Text("Aucun utilisateur trouvé pour ce filtre.", color = Color.DarkGray)
                    }
                } else {
                    items(filteredUsers) { user ->
                        var expanded by remember { mutableStateOf(false) }
                        var selectedUserRole by remember { mutableStateOf(user.role) }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                UserRow(Icons.Default.Person, "${user.nom} ${user.prenom}")
                                UserRow(Icons.Default.Email, user.email)
                                UserRow(Icons.Default.AccountCircle, "Rôle : $selectedUserRole")

                                Spacer(modifier = Modifier.height(8.dp))

                                Box {
                                    Button(
                                        onClick = { expanded = true },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF0288D1),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text("Changer rôle")
                                    }

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
                                        containerColor = Color(0xFFBBDEFB)
                                    ) {
                                        listOf("client", "admin").forEach { roleOption ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        roleOption,
                                                        color = if (roleOption == selectedUserRole) Color(0xFF0D47A1) else Color(0xFF1565C0),
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                },
                                                onClick = {
                                                    expanded = false
                                                    if (roleOption != user.role) {
                                                        selectedUserRole = roleOption
                                                        val updatedUser = user.copy(role = roleOption)
                                                        viewModel.updateUser(updatedUser) {
                                                            viewModel.loadAllUsers { users = it }
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserRow(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF0288D1),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
