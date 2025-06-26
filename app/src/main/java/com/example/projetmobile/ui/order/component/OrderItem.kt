package com.example.projetmobile.ui.order.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projetmobile.data.Entities.Order

@Composable
fun OrderItem(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Commande #${order.date.seconds}", style = MaterialTheme.typography.titleMedium)
            Text("Date: ${order.date.toDate()}", style = MaterialTheme.typography.bodySmall)
            Text("Total: ${order.totalPrice} DH", style = MaterialTheme.typography.bodyLarge)
            Text("Statut: ${order.status}", style = MaterialTheme.typography.labelLarge)


            Spacer(modifier = Modifier.height(8.dp))

            Text("Articles:", style = MaterialTheme.typography.labelMedium)
            order.items.forEach { item ->
                Text("- ${item.product.productTitle} x${item.quantity}")
            }
        }
    }
}