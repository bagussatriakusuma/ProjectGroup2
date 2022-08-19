package com.example.projectgroup2.data.api.auth

import com.example.projectgroup2.data.api.auth.getUser.GetUserResponse
import com.example.projectgroup2.data.api.auth.login.LoginRequest
import com.example.projectgroup2.data.api.auth.login.LoginResponse
import com.example.projectgroup2.data.api.auth.profile.ProfileRequest
import com.example.projectgroup2.data.api.auth.profile.ProfileResponse
import com.example.projectgroup2.data.api.auth.register.RegisterRequest
import com.example.projectgroup2.data.api.auth.register.RegisterResponse
import com.example.projectgroup2.data.api.auth.updateuser.UpdateUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AuthAPI {
    @POST("auth/register")
    suspend fun postRegister(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun postLogin(@Body request: LoginRequest): Response<LoginResponse>

    @GET("auth/user")
    suspend fun getUser(@Header("access_token") token: String): Response<GetUserResponse>

    @Multipart
    @PUT("auth/user")
    suspend fun updateUser(
        @Header("access_token") token: String,
        @Part file: MultipartBody.Part? = null,
        @Part("full_name") name: RequestBody?,
        @Part("phone_number") phoneNumber: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("email") email: RequestBody? = null,
        @Part("password") password: RequestBody? = null
    ): Response<UpdateUserResponse>
}