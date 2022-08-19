package com.example.projectgroup2.data.api

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null,
)
