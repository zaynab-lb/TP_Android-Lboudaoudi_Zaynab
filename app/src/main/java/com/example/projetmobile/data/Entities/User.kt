package com.example.projetmobile.data.Entities

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("userId")
    val id: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("age")
    val age: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("role")
    val role: String
)
{
}