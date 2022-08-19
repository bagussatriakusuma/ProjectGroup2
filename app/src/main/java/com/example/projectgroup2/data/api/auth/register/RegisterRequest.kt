package com.example.projectgroup2.data.api.auth.register

data class RegisterRequest(
    val full_name: String,
    val email: String,
    val password: String,
    val phone_number: String,
    val address: String,
    val city: String
)
