package com.klim.habrareader.domain.repositories

import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.domain.Response
import com.klim.habrareader.domain.UseCaseBase
import com.klim.habrareader.domain.entities.PostDetailsEntity
import com.klim.habrareader.domain.entities.PostThumbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface PostRepositoryI {

    fun getAllPosts(
        flow: MutableSharedFlow<PostThumbEntity>,
        cached: Boolean = false,
        remote: Boolean = false,
        postsThreshold: PostsThreshold,
        onComplete: (UseCaseBase.CompleteStatus) -> Unit
    )

    fun getBestPosts(
        flow: MutableSharedFlow<PostThumbEntity>,
        cached: Boolean = false,
        remote: Boolean = false,
        postsPeriod: PostsPeriod,
        onComplete: (UseCaseBase.CompleteStatus) -> Unit
    )

    fun getPostDetails(
        cached: Boolean,
        remote: Boolean,
        postId: Int,
        onComplete: (UseCaseBase.CompleteStatus) -> Unit
    )

    fun getPostDetails(
        cached: Boolean,
        remote: Boolean,
        postId: Int,
    ): Flow<Response<PostDetailsEntity>>

    fun savePostThumb(post: PostThumbEntity): Boolean

    fun savePostDetails(postDetails: PostDetailsEntity): Boolean
}