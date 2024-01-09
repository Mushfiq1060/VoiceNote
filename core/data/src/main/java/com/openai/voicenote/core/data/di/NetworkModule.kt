package com.openai.voicenote.core.data.di

import com.google.gson.GsonBuilder
import com.openai.voicenote.core.data.remote.RemoteDataSource
import com.openai.voicenote.core.data.remote.RemoteDataSourceImpl
import com.openai.voicenote.core.data.remote.api.ApiEndPoint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    private const val BASE_URL = "https://api.openai.com/v1/"

    @Provides
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .build()
    }

    @Provides
    fun providesRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource {
        return remoteDataSourceImpl
    }

    @Provides
    fun getApiEndPoint(retrofit: Retrofit): ApiEndPoint {
        return retrofit.create(ApiEndPoint::class.java)
    }

}