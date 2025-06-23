package com.example.projetmobile.data.Api

import com.example.projetmobile.data.Entities.User
import retrofit2.http.GET

interface UserApi
{
    @GET("users.json")
    suspend fun getUsers(): List<User>
}