package com.klim.habrareader.data.repositories.post.dataSources.remote

import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.data.repositories.post.dtos.PostThumbDTO
import com.klim.habrareader.data.repositories.post.PostDataSourceI
import com.klim.habrareader.data.repositories.post.dataSources.remote.parsers.ListPostsParser
import com.klim.habrareader.data.repositories.post.dtos.PostDetailsDTO
import com.klim.habrareader.data.repositories.post.dataSources.remote.parsers.PostParser
import com.klim.habrareader.data.retrofit.RetrofitProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class PostRemoteDataSource(var retrofit: RetrofitProvider) : PostDataSourceI {

    override fun getAllPosts(postsThreshold: PostsThreshold): Flow<PostThumbDTO> = flow {
        var period = ""
        when (postsThreshold._id) {
            PostsThreshold.MORE_THAN_ALL._id -> period = ""
            PostsThreshold.MORE_THAN_0._id -> period = "top0"
            PostsThreshold.MORE_THAN_10._id -> period = "top10"
            PostsThreshold.MORE_THAN_25._id -> period = "top25"
            PostsThreshold.MORE_THAN_50._id -> period = "top50"
            PostsThreshold.MORE_THAN_100._id -> period = "top100"
        }
        val messages: Call<String> = retrofit.allPostsApi.allPosts(period)

        val response: Response<String> = messages.execute()
        if (response.isSuccessful) {
            response.body()?.let { body ->
                ListPostsParser.parse(body).forEach {
                    emit(it)
                }
            }
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    override fun getBestPosts(postsPeriod: PostsPeriod): Flow<PostThumbDTO> = flow {
        var period = ""
        when (postsPeriod._id) {
            PostsPeriod.PERIOD_DAY._id -> period = "daily"
            PostsPeriod.PERIOD_WEEK._id -> period = "weekly"
            PostsPeriod.PERIOD_MONTH._id -> period = "monthly"
            PostsPeriod.PERIOD_YEAR._id -> period = "yearly"
            PostsPeriod.PERIOD_ALL_TIME._id -> period = "alltime"
        }
        val messages: Call<String> = retrofit.allPostsApi.topPosts(period)

        try {
            val response: Response<String> = messages.execute()
            if (response.code() == 200) {
                response.body()?.let { body ->
                    ListPostsParser.parse(body).forEach {
                        emit(it)
                    }
                }
            } else {
                throw Exception(response.errorBody().toString())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getPost(postsId: Int): PostDetailsDTO {
        val messages: Call<String> = retrofit.getPostDetailsApi().postDetails(postsId)
        val response: Response<String> = messages.execute()
        if (response.isSuccessful) {
            response.body()?.let { body ->
                return PostParser.parse(body)
            }
            throw Exception(response.errorBody().toString())
        } else {
            throw Exception(response.errorBody().toString())
        }
    }

    @Throws(Exception::class)
    override suspend fun getPost_S(postsId: Int): PostDetailsDTO? {
        val responseRaw = retrofit.postDetailsApi.postDetails_S(postsId)
        return PostParser.parse(responseRaw)
    }

    override fun savePostThumb(postDetails: PostThumbDTO): Boolean {
        throw UnsupportedOperationException("I can not do these")
    }

    override fun savePostDetails(postDetails: PostDetailsDTO): Boolean {
        throw UnsupportedOperationException("I can not do these")
    }

}