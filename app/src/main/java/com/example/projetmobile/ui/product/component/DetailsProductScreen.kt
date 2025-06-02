package com.example.projetmobile.ui.product.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projetmobile.data.Entities.Product

@Composable
fun DetailsScreen(product: Product, navController: NavController)
{
    Column(
    modifier = Modifier
    .fillMaxSize()
    .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
    )
    {
    Image(
        painter = painterResource(id = product.imageRes),
        contentDescription = product.title,
        modifier = Modifier.size(200.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = product.title.toString(),
        style = MaterialTheme.typography.headlineMedium
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Prix: ${product.price} €",
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Affichage détaillé du stock
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when {
            product.quantity == 0 -> {
                Text(
                    text = "RUPTURE DE STOCK",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Ce produit n'est actuellement pas disponible",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            product.quantity < 10 -> {
                Text(
                    text = "STOCK FAIBLE",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Plus que ${product.quantity} unité(s) disponible(s)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Commandez rapidement !",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            else -> {
                Text(
                    text = "DISPONIBLE",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${product.quantity} unités en stock",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Référence: ${product.id}",
        style = MaterialTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = { navController.navigateUp() },
        modifier = Modifier.fillMaxWidth(0.6f)
    ) {
        Text("Retour à la liste")
    }
}
}