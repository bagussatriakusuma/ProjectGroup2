package com.example.projectgroup2.data.api.auth.login

data class LoginResponse(
    val id: Int,
    val name: String,
    val email: String,
    val access_token: String
)