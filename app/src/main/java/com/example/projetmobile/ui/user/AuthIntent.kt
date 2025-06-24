package com.example.projetmobile.ui.user

sealed class AuthIntent {
    data class Login(val email: String, val password: String) : AuthIntent()
    data class Register(
        val firstName: String,
        val lastName: String,
        val age: Int,
        val email: String,
        val password: String,
        val role: String = "client" // Par défaut client
    ) : AuthIntent()
    object Logout : AuthIntent()
}