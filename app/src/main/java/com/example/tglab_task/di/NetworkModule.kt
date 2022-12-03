package com.example.tglab_task.di

import com.example.tglab_task.BuildConfig
import com.example.tglab_task.models.PlayerResponse
import com.example.tglab_task.models.TeamGamesResponse
import com.example.tglab_task.models.TeamsResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BuildConfig.SERVER_URL)
        .client(okHttpClient)
        .build()

    @[Provides Singleton]
    fun provideTGLabService(retrofit: Retrofit): TGLabApi =
        retrofit.create(TGLabApi::class.java)
}

interface TGLabApi {
    @GET("api/v1/teams")
    suspend fun getTeams(): TeamsResponse

    @GET("api/v1/games")
    suspend fun getTeamGames(
        @Query("team_id") teamId: String,
        @Query("page") page: Int,
    ): TeamGamesResponse

    @GET("api/v1/players")
    suspend fun getPlayerInfo(
        @Query("search") query: String,
        @Query("page") page: Int,
    ): PlayerResponse
}