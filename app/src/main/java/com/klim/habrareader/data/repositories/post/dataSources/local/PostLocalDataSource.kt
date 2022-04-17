package com.klim.habrareader.data.repositories.post.dataSources.local

import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.data.db.AppDatabase
import com.klim.habrareader.data.repositories.post.dtos.PostThumbDTO
import com.klim.habrareader.data.repositories.post.PostDataSourceI
import com.klim.habrareader.data.repositories.post.dataSources.local.mappers.PostThumbDbMapper
import com.klim.habrareader.data.repositories.post.dtos.PostDetailsDTO
import com.klim.habrareader.data.repositories.post.dataSources.local.mappers.PostDetailsDbMapper
import com.klim.habrareader.data.repositories.post.dataSources.local.mappers.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class PostLocalDataSource(val db: AppDatabase) : PostDataSourceI {

    override fun getAllPosts(postsThreshold: PostsThreshold): Flow<PostThumbDTO> = flow {
        var votesCountBar = 0
        when (postsThreshold._id) {
            PostsThreshold.MORE_THAN_0._id -> votesCountBar = 0
            PostsThreshold.MORE_THAN_10._id -> votesCountBar = 10
            PostsThreshold.MORE_THAN_25._id -> votesCountBar = 25
            PostsThreshold.MORE_THAN_50._id -> votesCountBar = 50
            PostsThreshold.MORE_THAN_100._id -> votesCountBar = 100
        }
        db.postDao.getAllPostsThumbs(votesCountBar)!!.forEach { post ->
            emit(PostThumbDbMapper.transform(post!!))
        }
    }

    override fun getBestPosts(postsPeriod: PostsPeriod): Flow<PostThumbDTO> = flow {
        val calendar = Calendar.getInstance()
        when (postsPeriod._id) {
            PostsPeriod.PERIOD_DAY._id -> calendar.add(Calendar.DAY_OF_MONTH, -1)
            PostsPeriod.PERIOD_WEEK._id -> calendar.add(Calendar.DAY_OF_MONTH, -7)
            PostsPeriod.PERIOD_MONTH._id -> calendar.add(Calendar.MONTH, -2) //fix this dates
            PostsPeriod.PERIOD_YEAR._id -> calendar.add(Calendar.YEAR, -1)
        }
        db.postDao.getBestPostsThumbs(calendar.timeInMillis)!!.forEach { post ->
            emit(PostThumbDbMapper.transform(post!!))
        }
    }

    override suspend fun getPost_S(postsId: Int): PostDetailsDTO? {
        return db.postDao.getPostDetails_S(postsId)?.map()
    }

    override fun getPost(postsId: Int): PostDetailsDTO? {
        return PostDetailsDbMapper.transform(db.postDao.getPostDetails(postsId))
    }

    override fun savePostThumb(postThumb: PostThumbDTO): Boolean {
        var description = ""
        if (db.postDao.postExist(postThumb.id)) {
            description = db.postDao.getPostDescription(postThumb.id)!!
        }
        db.postDao.add(PostThumbDbMapper.transform(postThumb, description))
        return true
    }

    override fun savePostDetails(postDetails: PostDetailsDTO): Boolean {
        var shortDescription = ""
        var postImage: String? = null
        if (db.postDao.postExist(postDetails.id)) {
            shortDescription = db.postDao.getPostShortDescription(postDetails.id)!!
            postImage = db.postDao.getPostImage(postDetails.id)
        }
        db.postDao.add(PostDetailsDbMapper.transform(postDetails, shortDescription, postImage))
        return true
    }
}


