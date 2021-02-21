package com.klim.habrareader.data.repositories.postsThumb.dataSources.remote

import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.data.repositories.postsThumb.PostThumbDTO
import com.klim.habrareader.data.repositories.postsThumb.PostThumbsDataSourceI
import com.klim.habrareader.data.repositories.postsThumb.dataSources.remote.parsers.ListPostsParser
import com.klim.habrareader.data.retrofit.RetrofitProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class PostThumbsRemoteDataSource(var retrofit: RetrofitProvider) : PostThumbsDataSourceI {

    override fun getAllPosts(postsThreshold: PostsThreshold): Flow<PostThumbDTO> = flow {
        var period = ""
        when (postsThreshold._id) {
            PostsThreshold.MORE_THAN_0._id -> period = "all"
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
            PostsPeriod.PERIOD_DAY._id -> period = ""
            PostsPeriod.PERIOD_WEEK._id -> period = "weekly"
            PostsPeriod.PERIOD_MONTH._id -> period = "monthly"
            PostsPeriod.PERIOD_YEAR._id -> period = "yearly"
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
}