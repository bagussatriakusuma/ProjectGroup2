package com.example.projectgroup2.data.api.auth.register

data class RegisterResponse(
    val address: String,
    val createdAt: String,
    val email: String,
    val full_name: String,
    val id: Int,
    val image_url: Any,
    val password: String,
    val phone_number: String,
    val updatedAt: String
)