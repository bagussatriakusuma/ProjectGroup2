package com.example.projectgroup2.data.api.main.buyerorder

import com.google.gson.annotations.SerializedName

data class BuyerOrderRequest(
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("bid_price")
    val bidPrice: Int
)