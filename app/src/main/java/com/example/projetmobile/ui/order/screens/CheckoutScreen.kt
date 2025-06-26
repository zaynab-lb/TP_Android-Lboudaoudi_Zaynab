package com.example.projetmobile.ui.order.screens

import android.R.attr.value
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projetmobile.nav.Routes
import com.example.projetmobile.ui.cart.CartViewModel
import com.example.projetmobile.ui.product.ProductViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val cartItems by cartViewModel.cartItems.collectAsState()
    val errorMessage by cartViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Informations de livraison", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom complet") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Adresse") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Informations de paiement", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Numéro de carte") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = expiryDate,
                onValueChange = { expiryDate = it },
                label = { Text("Date d'expiration (MM/AA)") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = cvv,
                onValueChange = { cvv = it },
                label = { Text("CVV") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                cartViewModel.validateOrder(
                    userId = userId,
                    name = name,
                    address = address
                ) {
                    navController.popBackStack(Routes.ClientHome, false)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && address.isNotBlank() &&
                    cardNumber.isNotBlank() && expiryDate.isNotBlank() && cvv.isNotBlank()
        ) {
            Text("Confirmer la commande")
        }

// Afficher les erreurs
        errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}