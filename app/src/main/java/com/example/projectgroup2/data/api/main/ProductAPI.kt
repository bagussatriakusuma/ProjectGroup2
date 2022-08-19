package com.example.projectgroup2.data.api.main

import com.example.projectgroup2.data.api.main.approveorder.ApproveOrderRequest
import com.example.projectgroup2.data.api.main.approveorder.ApproveOrderResponse
import com.example.projectgroup2.data.api.main.buyer.product.GetProductResponse
import com.example.projectgroup2.data.api.main.buyerorder.BuyerOrderRequest
import com.example.projectgroup2.data.api.main.buyerorder.BuyerOrderResponse
import com.example.projectgroup2.data.api.main.buyerorder.GetBuyerOrderResponse
import com.example.projectgroup2.data.api.main.category.CategoryResponse
import com.example.projectgroup2.data.api.main.deleteproduct.DeleteSellerProductResponse
import com.example.projectgroup2.data.api.main.notification.NotifResponse
import com.example.projectgroup2.data.api.main.updateproduct.UpdateProductResponse
import com.example.projectgroup2.data.api.main.productdetails.GetProductByIdResponse
import com.example.projectgroup2.data.api.main.sellerorder.SellerOrderResponse
import com.example.projectgroup2.data.api.main.sellerproduct.SellerProductResponse
import com.example.projectgroup2.data.api.main.uploadproduct.UploadProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProductAPI {
    //Buyer
    @GET("buyer/product")
    suspend fun getProductBuyer(
        @Query("status") status: String,
        @Query("category_id") categoryId: String,
        @Query("search") search: String
    ): Response<List<GetProductResponse>>

    @GET("buyer/product/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): Response<GetProductByIdResponse>

    @POST("buyer/order")
    suspend fun buyerOrder(
        @Header("access_token") token: String,
        @Body requestBuyerOrder: BuyerOrderRequest
    ): Response<BuyerOrderResponse>

    @GET("buyer/order")
    suspend fun getBuyerOrder(
        @Header("access_token") token: String
    ): Response<List<GetBuyerOrderResponse>>

    //Seller
    @GET("seller/category")
    suspend fun getCategory(): Response<List<CategoryResponse>>

    @Multipart
    @POST("seller/product")
    suspend fun uploadProduct(
        @Header("access_token") token: String,
        @Part file: MultipartBody.Part,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("base_price") base_price: RequestBody?,
        @Part("category_ids") categoryIds: List<Int>,
        @Part("location") location: RequestBody?,
    ): Response<UploadProductResponse>

    @GET("seller/product")
    suspend fun getSellerProduct(
        @Header("access_token") token: String
    ): Response<List<SellerProductResponse>>

    @GET("seller/order")
    suspend fun getSellerOrder(
        @Query("status") status: String,
        @Header("access_token") token: String
    ): Response<List<SellerOrderResponse>>

    @Multipart
    @PUT("seller/product/{id}")
    suspend fun updateProduct(
        @Header("access_token") token: String,
        @Path("id") id: Int,
        @Part file: MultipartBody.Part?,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("base_price") base_price: RequestBody?,
        @Part("category_ids") categoryIds: List<Int>,
        @Part("location") location: RequestBody?,
    ): Response<UpdateProductResponse>

    @DELETE("seller/product/{id}")
    suspend fun deleteSellerProduct(
        @Header("access_token") token: String,
        @Path("id") id: Int
    ): Response<DeleteSellerProductResponse>

    @PATCH("seller/order/{id}")
    suspend fun approveOrder(
        @Header("access_token") token: String,
        @Path("id") id: Int,
        @Body approveOrderRequest: ApproveOrderRequest
    ): Response<ApproveOrderResponse>

    @GET("notification")
    suspend fun getNofitication(
        @Header("access_token") token: String
    ): Response<List<NotifResponse>>

    @GET("notification/{id}")
    suspend fun getNofiticationById(
        @Header("access_token") token: String,
        @Path("id") id: String
    ): Response<List<NotifResponse>>

    @PATCH("notification/{id}")
    suspend fun markReadNotification(
        @Path("id") id: Int
    )

}