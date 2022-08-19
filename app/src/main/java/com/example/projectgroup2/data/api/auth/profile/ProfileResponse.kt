package com.example.projectgroup2.data.api.auth.profile

data class ProfileResponse(
    val address: String,
    val city: String,
    val createdAt: String,
    val email: String,
    val full_name: String,
    val id: Int,
    val image_url: String,
    val password: String,
    val phone_number: Long,
    val updatedAt: String
)