package com.example.projectgroup2.di

import com.example.projectgroup2.data.api.auth.AuthAPI
import com.example.projectgroup2.data.api.image.ImageAPI
import com.example.projectgroup2.data.local.UserDAO
import com.example.projectgroup2.datastore.AuthDataStoreManager
import com.example.projectgroup2.repository.AuthRepository
import com.example.projectgroup2.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideAuthRepository(
        authDataStoreManager: AuthDataStoreManager,
        api: AuthAPI,
        dao: UserDAO
    ): AuthRepository {
        return AuthRepository(
            authDataStore = authDataStoreManager,
            api = api,
            dao = dao
        )
    }

//    @Singleton
//    @Provides
//    fun provideProfileRepository(
//        imageAPI: ImageAPI,
//        authAPI: AuthAPI,
//        userDAO: UserDAO,
//        @Named(Constant.ApiKeyImgBB) apikey: String
//    ): ProfileRepository {
//        return ProfileRepository(
//            imageAPI = imageAPI,
//            authAPI = authAPI,
//            dao = userDAO,
//            apiKey = apikey
//        )
//    }
}