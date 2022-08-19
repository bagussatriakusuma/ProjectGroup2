package com.example.projectgroup2.data.api.main.buyerorder

import com.google.gson.annotations.SerializedName

data class BuyerOrderResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("buyer_id")
    val buyerId: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("message")
    val message: String
)