package com.klim.habrareader.data.repositories.postsThumb

import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import kotlinx.coroutines.flow.Flow

interface PostThumbsDataSourceI {
    fun getAllPosts(postsThreshold: PostsThreshold): Flow<PostThumbDTO>

    fun getBestPosts(postsPeriod: PostsPeriod): Flow<PostThumbDTO>
}