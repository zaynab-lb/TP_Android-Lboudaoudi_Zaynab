package com.example.projetmobile.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetmobile.data.Entities.User
import com.example.projetmobile.data.Repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AuthViewState())
    val state: StateFlow<AuthViewState> = _state

    fun handleIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Login -> login(intent.email, intent.password)
            is AuthIntent.Register -> register(
                intent.firstName,
                intent.lastName,
                intent.age,
                intent.email,
                intent.password,
                intent.role
            )
            AuthIntent.Logout -> logout()
        }
    }

    private fun login(email: String, password: String) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val user = userRepository.authenticate(email, password)
                _state.value = if (user != null) {
                    AuthViewState(
                        isAuthenticated = true,
                        currentUser = user
                    )
                } else {
                    AuthViewState(error = "Email ou mot de passe incorrect")
                }
            } catch (e: Exception) {
                _state.value = AuthViewState(error = "Erreur de connexion")
            }
        }
    }

    private fun register(
        firstName: String,
        lastName: String,
        age: Int,
        email: String,
        password: String,
        role: String
    ) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val newUser = User(
                    id = (userRepository.getUsers().size + 1).toString(),
                    firstName = firstName,
                    lastName = lastName,
                    age = age,
                    email = email,
                    password = password,
                    role = role
                )

                if (userRepository.registerUser(newUser)) {
                    _state.value = AuthViewState(
                        isAuthenticated = true,
                        currentUser = newUser,
                        isLoading = false
                    )
                } else {
                    _state.value = AuthViewState(error = "Cet email est déjà utilisé")
                }
            } catch (e: Exception) {
                _state.value = AuthViewState(error = "Erreur lors de l'inscription")
            }
        }
    }


    private fun logout() {
        _state.value = AuthViewState(
            isAuthenticated = false,
            currentUser = null
        )
    }
}