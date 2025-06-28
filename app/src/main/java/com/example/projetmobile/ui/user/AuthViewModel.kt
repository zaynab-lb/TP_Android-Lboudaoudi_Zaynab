package com.example.projetmobile.ui.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetmobile.data.Entities.User
import com.example.projetmobile.data.firebase.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    // Ajout du StateFlow pour le nombre de clients
    public val _clientCount = MutableStateFlow(0)
    val clientCount: StateFlow<Int> = _clientCount

    fun loadClientCount() {
        viewModelScope.launch {
            try {
                val users = authService.getAllUsers() // récupère tous les utilisateurs
                val clients = users.filter { it.role.equals("client", ignoreCase = true) }
                _clientCount.value = clients.size
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Erreur chargement clientCount", e)
                _clientCount.value = 0
            }
        }
    }

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

    fun updatePassword(currentPassword: String, newPassword: String?, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = authService.updatePassword(currentPassword, newPassword)
            onResult(success)
        }
    }


    fun loadAllUsers(onResult: (List<User>) -> Unit) {
        viewModelScope.launch {
            val users = authService.getAllUsers()
            onResult(users)
        }
    }

    fun updateUserRole(userId: String, newRole: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = authService.updateUserRole(userId, newRole)
            onResult(success)
        }
    }


}
