package com.example.projectgroup2.data.api.auth.profile

data class ProfileRequest(
    val full_name: String,
    val email: String,
    val password: String,
    val phone_number: Long,
    val address: String,
    val image_url: String,
    val city: String
)
