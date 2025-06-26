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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.projetmobile.ui.user.AuthViewModel

@Composable
fun LoginScreen(
    onClientLogin: () -> Unit,
    onAdminLogin: () -> Unit,
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
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
                        Color(0xFFB3E5FC),
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
                    text = "Se connecter",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF0288D1),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                        viewModel.login(email, password) { user ->
                            if (user != null) {
                                if (user.role == "admin") onAdminLogin() else onClientLogin()
                            } else {
                                error = "Identifiants incorrects"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                ) {
                    Text(text = "Se connecter", color = Color.White, fontSize = 18.sp)
                }

                if (error.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Pas encore de compte ? Cliquez ici pour vous inscrire",
                    color = Color(0xFF0288D1),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("signup") }
                        .padding(vertical = 8.dp),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
