package com.example.projetmobile.ui.product.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.projetmobile.data.Entities.Product

@Composable
fun ProductItem(product: Product, onDetailsClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //val imageUrl = "https://raw.githubusercontent.com/zaynab-lb/TP_Android-Lboudaoudi_Zaynab/master/app/public/images/clavier.jpeg"
            Image(
               // painter = painterResource(id = product.imageRes),
                painter = rememberAsyncImagePainter(model = product.imageRes),
                contentDescription = product.title,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.title.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = product.category ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "${product.price} DH",
                    style = MaterialTheme.typography.bodyLarge
                )

                // Affichage conditionnel du stock dans la liste
                when {
                    product.quantity == 0 -> {
                        Text(
                            text = "RUPTURE DE STOCK",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    product.quantity < 10 -> {
                        Text(
                            text = "Stock faible (${product.quantity})",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    else -> {
                        Text(
                            text = "En stock",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Button(onClick = onDetailsClick) {
                Text("Détails")
            }
        }
    }
}