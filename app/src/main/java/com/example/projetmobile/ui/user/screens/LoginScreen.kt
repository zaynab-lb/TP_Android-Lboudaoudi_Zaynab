package com.example.projetmobile.ui.user.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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

    Column(Modifier.padding(16.dp)) {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(onClick = {
            viewModel.login(email, password) { user ->
                if (user != null) {
                    if (user.role == "admin") onAdminLogin() else onClientLogin()
                } else {
                    error = "Identifiants incorrects"
                }
            }
        }) {
            Text("Se connecter")
        }

        Text(error, color = Color.Red)

        Text(
            text = "Pas encore de compte ? Cliquez ici pour vous inscrire",
            color = Color.Blue,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    navController.navigate("signup")
                }
        )

    }
}
