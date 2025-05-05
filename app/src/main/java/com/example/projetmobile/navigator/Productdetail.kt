package com.example.projetmobile.navigator

import com.example.projetmobile.R
import com.example.projetmobile.model.Product

object Routes {
    const val Home = "home"
    const val ProductDetails = "productDetails"

    val Products = listOf(
        Product(
            id = "1",
            title = "Clavier",
            price = 150.00,
            quantity = 0,
            imageRes = R.drawable.clavier
        ),
        Product(
            id = "2",
            title = "Souris",
            price = 75.00,
            quantity = 5,
            imageRes = R.drawable.souris
        ),
        Product(
            id = "3",
            title = "Ecran",
            price = 249.99,
            quantity = 50,
            imageRes = R.drawable.ecran
        ),
        Product(
            id = "4",
            title = "PC Portable",
            price = 6999.99,
            quantity = 111,
            imageRes = R.drawable.pc
        ),
        Product(
            id = "5",
            title = "Tapis",
            price = 50.99,
            quantity = 100,
            imageRes = R.drawable.tapis
        )
    )

    // Fonction utilitaire pour trouver un produit par son ID
    fun getProductById(productId: String): Product? {
        return Products.find { it.id == productId }
    }
}