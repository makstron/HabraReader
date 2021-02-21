package com.klim.habrareader.data.repositories.post.dataSources.remote

import com.klim.habrareader.data.repositories.post.PostDTO
import com.klim.habrareader.data.repositories.post.dataSources.PostDataSourceI
import com.klim.habrareader.data.retrofit.RetrofitProvider

class PostRemoteDataSource(var retrofit: RetrofitProvider) : PostDataSourceI {

    override fun getPost(postsId: Int): PostDTO {
        TODO("Not yet implemented")
    }

    override fun savePost(post: PostDTO): Boolean {
        throw UnsupportedOperationException("I can not do these")
    }

}


