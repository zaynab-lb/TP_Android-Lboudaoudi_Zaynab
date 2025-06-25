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

    Column(Modifier.padding(16.dp)) {
        TextField(value = nom, onValueChange = { nom = it }, label = { Text("Nom") })
        TextField(value = prenom, onValueChange = { prenom = it }, label = { Text("Prénom") })
        TextField(value = age, onValueChange = { age = it }, label = { Text("Âge") })
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation()
        )


        Button(onClick = {
            viewModel.signUpClient(
                nom = nom, prenom = prenom, age = age.toIntOrNull() ?: 0,
                email = email, password = password,
                onSuccess = onSignupSuccess
            )
        }) {
            Text("S'inscrire")
        }
        Text(
            text = "Déjà un compte ? Connectez-vous",
            color = Color.Blue,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    navController.navigate(Routes.Login)
                }
        )

    }
}
