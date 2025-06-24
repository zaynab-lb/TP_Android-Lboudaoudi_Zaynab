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
import coil.compose.rememberAsyncImagePainter
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
        //painter = painterResource(id = product.imageRes),
        painter = rememberAsyncImagePainter(model = product.productImageRes),
        contentDescription = product.productTitle,
        modifier = Modifier.size(200.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = product.productTitle.toString(),
        style = MaterialTheme.typography.headlineMedium
    )

    Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Catégorie: ${product.productCategory}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Prix: ${product.productPrice} DH",
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Description:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = product.productDescription ?: "Aucune description disponible",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

    // Affichage détaillé du stock
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when {
            product.productQuantity == 0 -> {
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
            product.productQuantity < 10 -> {
                Text(
                    text = "STOCK FAIBLE",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Plus que ${product.productQuantity} unité(s) disponible(s)",
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
                    text = "${product.productQuantity} unités en stock",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }


    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Référence: ${product.productID}",
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