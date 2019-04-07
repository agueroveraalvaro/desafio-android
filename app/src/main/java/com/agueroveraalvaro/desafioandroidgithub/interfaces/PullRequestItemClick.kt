package com.agueroveraalvaro.desafioandroidgithub.interfaces

import com.agueroveraalvaro.desafioandroidgithub.model.PullRequest

interface PullRequestItemClick
{
    abstract fun onPullRequestItemClick(pullRequest: PullRequest)
}