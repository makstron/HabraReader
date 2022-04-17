package com.klim.habrareader.data.repositories.post

import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.data.repositories.post.dtos.PostThumbDTO
import com.klim.habrareader.data.repositories.post.dtos.PostDetailsDTO
import kotlinx.coroutines.flow.Flow

interface PostDataSourceI {
    fun getAllPosts(postsThreshold: PostsThreshold): Flow<PostThumbDTO>

    fun getBestPosts(postsPeriod: PostsPeriod): Flow<PostThumbDTO>

    fun getPost(postsId: Int): PostDetailsDTO?
    suspend fun getPost_S(postsId: Int): PostDetailsDTO?

    fun savePostThumb(postDetails: PostThumbDTO): Boolean

    fun savePostDetails(postDetails: PostDetailsDTO): Boolean
}