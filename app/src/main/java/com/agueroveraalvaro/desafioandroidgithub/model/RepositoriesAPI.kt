package com.agueroveraalvaro.desafioandroidgithub.model

import com.google.gson.annotations.SerializedName

data class RepositoriesAPI(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val repositories: List<Repository>,
    @SerializedName("total_count")
    val totalCount: Int
)