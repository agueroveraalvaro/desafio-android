package com.agueroveraalvaro.desafioandroidgithub.interfaces

import com.agueroveraalvaro.desafioandroidgithub.model.ApiGitHubJSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface InterfaceAPI
{
    @GET("search/repositories")
    fun getRepositories(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("page") page: Int
    ) : Call<ApiGitHubJSON>

    /*@GET("repos/{user}/{repository}/pulls")
    fun getPullRequestRepository(
        @Path("user") user: String,
        @Path("repository") repository: String
    ) : Call<List<PullRequest>>*/
}