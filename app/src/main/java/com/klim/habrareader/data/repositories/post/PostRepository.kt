package com.klim.habrareader.data.repositories.post

import com.klim.habrareader.data.repositories.post.dataSources.PostDataSourceI
import com.klim.habrareader.domain.entities.PostEntity
import com.klim.habrareader.domain.repositories.PostRepositoryI

class PostRepository(
    private var localDataSource: PostDataSourceI,
    private var remoteDataSource: PostDataSourceI
) : PostRepositoryI {
    override suspend fun getPost(cached: Boolean, remote: Boolean, postId: Int): PostEntity {
        TODO("Not yet implemented")
    }

    override fun savePost(post: PostEntity): Boolean {
        localDataSource.savePost(PostDomainMapper.transform(post))
        return true
    }
}