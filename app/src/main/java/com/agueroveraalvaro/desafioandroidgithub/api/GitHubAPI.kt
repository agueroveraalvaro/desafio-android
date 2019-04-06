package com.agueroveraalvaro.desafioandroidgithub.api

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
                .baseUrl("https://api.github.com/")
                .build()
        }
        return retrofit
    }
}