package com.example.projectgroup2.data.api.main.approveorder

import com.google.gson.annotations.SerializedName

data class ApproveOrderRequest(
    @SerializedName("status")
    val status: String
)
