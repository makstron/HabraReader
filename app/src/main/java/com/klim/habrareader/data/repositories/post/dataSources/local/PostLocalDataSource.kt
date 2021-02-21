package com.klim.habrareader.data.repositories.post.dataSources.local

import com.klim.habrareader.data.db.AppDatabase
import com.klim.habrareader.data.repositories.post.PostDTO
import com.klim.habrareader.data.repositories.post.dataSources.PostDataSourceI

class PostLocalDataSource(val db: AppDatabase) : PostDataSourceI {

    override fun getPost(postsId: Int): PostDTO {
        return PostDbMapper.transform(db.postDao.getPost(postsId))
    }

    override fun savePost(post: PostDTO): Boolean {
        db.postDao.add(PostDbMapper.transform(post))
        return true
    }

}


