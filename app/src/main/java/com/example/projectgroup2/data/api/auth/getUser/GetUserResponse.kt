package com.example.projectgroup2.data.api.auth.getUser

import com.google.gson.annotations.SerializedName

data class GetUserResponse(
    @SerializedName("id"           ) var id          : Int?    = null,
    @SerializedName("full_name"    ) var fullName    : String? = null,
    @SerializedName("email"        ) var email       : String? = null,
    @SerializedName("password"     ) var password    : String? = null,
    @SerializedName("phone_number" ) var phoneNumber : String? = null,
    @SerializedName("city"         ) var city        : String? = null,
    @SerializedName("address"      ) var address     : String? = null,
    @SerializedName("image_url"    ) var imageUrl    : String? = null
)