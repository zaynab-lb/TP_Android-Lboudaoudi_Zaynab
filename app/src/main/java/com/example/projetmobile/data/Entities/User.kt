package com.example.projetmobile.data.Entities

data class User(
    val id: String = "",
    val nom: String = "",
    val prenom: String = "",
    val age: Int = 0,
    val email: String = "",
    val role: String = "client"
)
