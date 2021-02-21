package com.klim.habrareader.domain.useCaseInterfaces

import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.domain.UseCaseBase
import com.klim.habrareader.domain.entities.PostThumbEntity
import kotlinx.coroutines.flow.MutableSharedFlow

interface PostThumbsUseCaseI {
    suspend fun getAllPosts(
        cached: Boolean,
        remote: Boolean,
        postsThreshold: PostsThreshold,
        onComplete: (UseCaseBase.CompleteStatus) -> Unit
    )

    suspend fun getBestPosts(
        cached: Boolean,
        remote: Boolean,
        postsPeriod: PostsPeriod,
        onComplete: (UseCaseBase.CompleteStatus) -> Unit
    )
}