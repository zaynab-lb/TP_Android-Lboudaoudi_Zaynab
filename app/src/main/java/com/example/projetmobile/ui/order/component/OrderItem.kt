package com.example.projetmobile.ui.order.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projetmobile.data.Entities.Order

@Composable
fun OrderItem(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Commande #${order.date.seconds}",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF0288D1)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Date : ${order.date.toDate()}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Total : ${order.totalPrice} DH",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Statut : ${order.status}",
                style = MaterialTheme.typography.labelLarge,
                color = when (order.status.lowercase()) {
                    "en attente" -> MaterialTheme.colorScheme.secondary
                    "validée", "livrée" -> Color(0xFF388E3C) // vert
                    "annulée" -> MaterialTheme.colorScheme.error
                    else -> Color.DarkGray
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Articles :",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF0288D1)
            )

            Spacer(modifier = Modifier.height(4.dp))

            order.items.forEach { item ->
                Text(
                    text = "- ${item.product.productTitle} x${item.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
        }
    }
}
