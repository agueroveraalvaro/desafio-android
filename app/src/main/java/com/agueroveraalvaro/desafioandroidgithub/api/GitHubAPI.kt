package com.agueroveraalvaro.desafioandroidgithub.api

import com.agueroveraalvaro.desafioandroidgithub.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GitHubAPI
{
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit?
    {
        if (retrofit == null)
        {
            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_URL)
                .build()
        }
        return retrofit
    }
}