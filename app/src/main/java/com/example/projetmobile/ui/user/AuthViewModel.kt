package com.example.projetmobile.ui.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetmobile.data.Entities.User
import com.example.projetmobile.data.firebase.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    fun signUpClient(nom: String, prenom: String, age: Int, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = User(nom = nom, prenom = prenom, age = age, email = email)
            val success = authService.signUpClient(user, password)
            if (success) {
                onSuccess()
            } else {
                Log.e("AuthViewModel", "Échec de la création de compte")
            }
        }
    }


    fun login(email: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = authService.login(email, password)
            onResult(user)
        }
    }

    fun logout() = authService.logout()

    fun loadCurrentUser(onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = authService.getCurrentUser()
            onResult(user)
        }
    }

    fun updateUser(user: User, onResult: () -> Unit) {
        viewModelScope.launch {
            val userId = user.id
            authService.updateUser(userId, user)
            onResult()
        }
    }


}
