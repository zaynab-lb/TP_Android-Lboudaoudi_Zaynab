package com.example.projetmobile.ui.user.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.nav.Routes
import com.example.projetmobile.ui.user.AuthViewModel

@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var nom by remember { mutableStateOf("") }
    var prenom by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE1F5FE),
                        Color(0xFFB3E5FC)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            shadowElevation = 10.dp,
            color = Color.White.copy(alpha = 0.95f)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "S'inscrire",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF0288D1),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = nom,
                    onValueChange = { nom = it },
                    label = { Text("Nom") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = prenom,
                    onValueChange = { prenom = it },
                    label = { Text("Prénom") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Âge") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mot de passe") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (age.toIntOrNull() == null) {
                            error = "Veuillez entrer un âge valide"
                        } else {
                            viewModel.signUpClient(
                                nom = nom,
                                prenom = prenom,
                                age = age.toInt(),
                                email = email,
                                password = password,
                                onSuccess = onSignupSuccess
                            )
                            error = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                ) {
                    Text(text = "S'inscrire", color = Color.White, fontSize = 18.sp)
                }

                if (error.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Déjà un compte ? Connectez-vous",
                    color = Color(0xFF0288D1),
                    modifier = Modifier
                        .clickable { navController.navigate(Routes.Login) }
                        .padding(vertical = 8.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}
