package com.klim.habrareader.domain.repositories

import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.domain.UseCaseBase
import com.klim.habrareader.domain.entities.PostThumbEntity
import kotlinx.coroutines.flow.MutableSharedFlow

interface PostThumbRepositoryI {

    suspend fun getAllPosts(
        flow: MutableSharedFlow<PostThumbEntity>,
        cached: Boolean = false,
        remote: Boolean = false,
        postsThreshold: PostsThreshold,
        onComplete: (UseCaseBase.CompleteStatus) -> Unit
    )

    suspend fun getBestPosts(
        flow: MutableSharedFlow<PostThumbEntity>,
        cached: Boolean = false,
        remote: Boolean = false,
        postsPeriod: PostsPeriod,
        onComplete: (UseCaseBase.CompleteStatus) -> Unit
    )
}