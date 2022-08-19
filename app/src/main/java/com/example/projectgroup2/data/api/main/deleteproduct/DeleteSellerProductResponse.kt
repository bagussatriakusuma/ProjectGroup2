package com.example.projectgroup2.data.api.main.deleteproduct

import com.google.gson.annotations.SerializedName

data class DeleteSellerProductResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("msg")
    val msg: String
)
