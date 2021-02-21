package com.klim.habrareader.data.repositories.post.dataSources

import com.klim.habrareader.data.repositories.post.PostDTO

interface PostDataSourceI {
    fun getPost(postsId: Int): PostDTO

    fun savePost(post: PostDTO): Boolean
}