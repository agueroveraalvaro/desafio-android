package com.agueroveraalvaro.desafioandroidgithub.interfaces

import com.agueroveraalvaro.desafioandroidgithub.model.RepositoriesAPI
import com.agueroveraalvaro.desafioandroidgithub.model.PullRequest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InterfaceAPI
{
    @GET("search/repositories")
    fun getRepositories(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("page") page: Int
    ) : Call<RepositoriesAPI>

    @GET("repos/{user}/{repository}/pulls")
    fun getPullRequestsRepository(
        @Path("user") user: String,
        @Path("repository") repository: String
    ) : Call<List<PullRequest>>
}