package com.example.projetmobile.data.Repository

import com.example.projetmobile.data.Api.UserApi
import com.example.projetmobile.data.Entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val api: UserApi) {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadInitialUsers()
        }
    }

    private suspend fun loadInitialUsers() {
        val apiUsers = api.getUsers()
        _users.value = apiUsers
    }
    suspend fun getUsers(): List<User> = users.value

    suspend fun authenticate(email: String, password: String): User? {
        return users.value.find { it.email == email && it.password == password }
    }


    suspend fun registerUser(newUser: User): Boolean {
        return if (users.value.any { it.email == newUser.email }) {
            false // Email déjà utilisé
        } else {
            _users.value = users.value + newUser
            true
        }
    }
}