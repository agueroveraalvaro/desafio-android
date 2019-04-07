package com.agueroveraalvaro.desafioandroidgithub.interfaces

import com.agueroveraalvaro.desafioandroidgithub.model.Repository

interface RepositoryItemClick
{
    abstract fun onRepositoryItemClick(repository: Repository)
}