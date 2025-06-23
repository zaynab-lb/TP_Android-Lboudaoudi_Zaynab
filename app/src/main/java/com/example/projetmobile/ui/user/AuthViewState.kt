package com.example.projetmobile.ui.user

import com.example.projetmobile.data.Entities.User

data class AuthViewState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val currentUser: User? = null,
    val error: String? = null
)