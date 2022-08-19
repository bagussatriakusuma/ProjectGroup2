package com.example.projectgroup2.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.projectgroup2.data.api.auth.AuthAPI
import com.example.projectgroup2.data.api.image.ImageAPI
import com.example.projectgroup2.data.api.main.ProductAPI
import com.example.projectgroup2.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun provideHttpLogging(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        return httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context
    ) : ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    @Named(Constant.RetrofitAuth)
    fun provideRetrofitAuth(client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    @Named(Constant.RetrofitProduct)
    fun provideRetrofitProduct(client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    @Named(Constant.RetrofitImgBB)
    fun provideRetrofitImgBB(client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.baseUrlImgBB)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthAPI(@Named(Constant.RetrofitAuth) retrofit: Retrofit) : AuthAPI {
        return retrofit.create(AuthAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideProductAPI(@Named(Constant.RetrofitProduct) retrofit: Retrofit) : ProductAPI {
        return retrofit.create(ProductAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthImgBB(@Named(Constant.RetrofitImgBB) retrofit: Retrofit) : ImageAPI {
        return retrofit.create(ImageAPI::class.java)
    }
}