package com.example.projetmobile.data.Repository

import com.example.projetmobile.data.Api.UserApi
import com.example.projetmobile.data.Entities.User
import javax.inject.Inject

class UserRepository @Inject constructor(private val api: UserApi) {
    suspend fun getUsers(): List<User> = api.getUsers()

    suspend fun authenticate(email: String, password: String): User? {
        return getUsers().find { it.email == email && it.password == password }
    }
}