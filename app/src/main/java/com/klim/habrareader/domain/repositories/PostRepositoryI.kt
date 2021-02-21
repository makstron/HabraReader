package com.klim.habrareader.domain.repositories

import com.klim.habrareader.domain.entities.PostEntity

interface PostRepositoryI {

    suspend fun getPost(
        cached: Boolean = false,
        remote: Boolean = false,
        postId: Int
    ): PostEntity

    fun savePost(post: PostEntity): Boolean
}