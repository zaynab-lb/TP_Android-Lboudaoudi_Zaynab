package com.example.projetmobile.ui.user

sealed class AuthIntent {
    data class Login(val email: String, val password: String) : AuthIntent()
    object Logout : AuthIntent()
}