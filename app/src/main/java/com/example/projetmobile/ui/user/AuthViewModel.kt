package com.example.projetmobile.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private fun logout() {
        _state.value = AuthViewState()
    }
}