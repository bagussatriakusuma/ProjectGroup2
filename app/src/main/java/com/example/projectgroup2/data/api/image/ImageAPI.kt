package com.example.projectgroup2.data.api.image

import com.example.projectgroup2.utils.Constant
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ImageAPI {
    @POST("upload")
    @Multipart
    suspend fun uploadImage(
        @Query("expiration") expiration: Int = 10000,
        @Query("key") key: String = Constant.ApiKeyImgBB,
        @Part image: MultipartBody.Part
    ): Response<ImageDataResponse>
}