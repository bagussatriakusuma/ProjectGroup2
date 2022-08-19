package com.example.projectgroup2.data.api.main.buyer.product

data class ProductResponseItem(
    val base_price: Int,
    val categories: List<Category>,
    val created_at: String,
    val id: Int,
    val image_name: String,
    val image_url: String,
    val location: String,
    val name: String,
    val updated_at: String,
    val user_id: Int
)