package com.klim.habrareader.app.windows.postDetails

import android.app.Application
import androidx.lifecycle.*
import com.klim.habrareader.R
import com.klim.habrareader.app.utils.htmlParsers.PostDetailsParser_v2
import com.klim.habrareader.app.windows.postDetails.entities.DetailBase
import com.klim.habrareader.data.db.DbHelper
import com.klim.habrareader.data.repositories.post.PostRepository
import com.klim.habrareader.data.repositories.post.dataSources.local.PostLocalDataSource
import com.klim.habrareader.data.repositories.post.dataSources.remote.PostRemoteDataSource
import com.klim.habrareader.data.retrofit.RetrofitProvider
import com.klim.habrareader.domain.Response
import com.klim.habrareader.domain.UseCaseBase
import com.klim.habrareader.domain.entities.PostDetailsEntity
import com.klim.habrareader.domain.useCaseInterfaces.GetPostDetailsUseCaseI
import com.klim.habrareader.domain.useCases.GetPostDetailsUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PostDetailsFragmentVM(val appContext: Application) : AndroidViewModel(appContext) {
    private val formatTime: DateFormat = SimpleDateFormat("HH:mm")
    private val formatDateTime: DateFormat = SimpleDateFormat("d MMMM yyyy в HH:mm")

    var postId: Int = -1

    private val _data = MutableLiveData<PostDetailsView>()
    val data: LiveData<PostDetailsView> = _data
    private var _isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val getPostDetailUseCase: GetPostDetailsUseCase = GetPostDetailsUseCase(
        viewModelScope,
        PostRepository(viewModelScope, PostLocalDataSource(DbHelper.get()), PostRemoteDataSource(RetrofitProvider()))
    )

    fun updatePost(showProgress: Boolean) {
        if (postId > 0) {
            if (showProgress) {
                _isLoading.value = true
            }
//            getPostDetailUseCase.getPostDetail(true, true, postId, ::loadingComplete)


            viewModelScope.launch(Dispatchers.Default) {
                getPostDetailUseCase.getPostDetail(true, true, postId)
                    .collect {
                        when (it) {
                            is Response.Success -> {

                                val post = it.data as PostDetailsEntity
                                val postDetailsList = ArrayList<DetailBase>()
                                if (!post.description.isNullOrEmpty()) {
//                        postDetailsList.addAll(PostDetailsParser.parse(post.description))

                                    var parser = PostDetailsParser_v2()
                                    postDetailsList.addAll(parser.parse(post.description))
                                }
                                withContext(Dispatchers.Main) {

                                    //todo refactor it
                                    val calToday = Calendar.getInstance()
                                    val calYesterday = Calendar.getInstance()
                                    calYesterday.add(Calendar.DATE, -1)
                                    val calPost = Calendar.getInstance()
                                    calPost.timeInMillis = post.createdTimestamp
                                    calPost.add(Calendar.HOUR_OF_DAY, 2) // 2 - is current timebelt

                                    val createdLabel: String
                                    when {
                                        calPost.get(Calendar.DAY_OF_MONTH) == calToday.get(Calendar.DAY_OF_MONTH) -> {
                                            createdLabel = "${appContext.getString(R.string.post_time_today)} ${appContext.getString(R.string.post_time_at)} ${formatTime.format(calPost.time)}"
                                        }
                                        calPost.get(Calendar.DAY_OF_MONTH) == calYesterday.get(Calendar.DAY_OF_MONTH) -> {
                                            createdLabel = "${appContext.getString(R.string.post_time_yesterday)} ${appContext.getString(R.string.post_time_at)} ${formatTime.format(calPost.time)}"
                                        }
                                        else -> {
                                            createdLabel = formatDateTime.format(calPost.time)
                                        }
                                    }

                                    val postView = PostDetailsView(
                                        title = post.title,
                                        postImageUrl = post.postImageUrl,
                                        authorNickname = post.authorLogin,
                                        authorAvatar = post.authorIcon,
                                        votesCount = post.votesCount,
                                        bookmarksCount = post.bookmarksCount,
                                        viewsCount = post.viewsCount,
                                        commentsCount = post.commentsCount,
                                        createdTimestamp = createdLabel,
                                        content = postDetailsList,
                                    )
                                    _data.value = postView
                                }

                            }
                            is Response.Error -> {
                                //todo show errors
                            }
                        }

                    }
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    private fun loadingComplete(status: UseCaseBase.CompleteStatus) {
        viewModelScope.launch(Dispatchers.Default) {
            if (status.isSuccessful) {
                status.data?.let {
                    val post = status.data as PostDetailsEntity
                    val postDetailsList = ArrayList<DetailBase>()
                    if (!post.description.isNullOrEmpty()) {
//                        postDetailsList.addAll(PostDetailsParser.parse(post.description))

                        var parser = PostDetailsParser_v2()
                        postDetailsList.addAll(parser.parse(post.description))
                    }
                    withContext(Dispatchers.Main) {

                        //todo refactor it
                        val calToday = Calendar.getInstance()
                        val calYesterday = Calendar.getInstance()
                        calYesterday.add(Calendar.DATE, -1)
                        val calPost = Calendar.getInstance()
                        calPost.timeInMillis = post.createdTimestamp
                        calPost.add(Calendar.HOUR_OF_DAY, 2) // 2 - is current timebelt

                        val createdLabel: String
                        when {
                            calPost.get(Calendar.DAY_OF_MONTH) == calToday.get(Calendar.DAY_OF_MONTH) -> {
                                createdLabel = "${appContext.getString(R.string.post_time_today)} ${appContext.getString(R.string.post_time_at)} ${formatTime.format(calPost.time)}"
                            }
                            calPost.get(Calendar.DAY_OF_MONTH) == calYesterday.get(Calendar.DAY_OF_MONTH) -> {
                                createdLabel = "${appContext.getString(R.string.post_time_yesterday)} ${appContext.getString(R.string.post_time_at)} ${formatTime.format(calPost.time)}"
                            }
                            else -> {
                                createdLabel = formatDateTime.format(calPost.time)
                            }
                        }

                        val postView = PostDetailsView(
                            title = post.title,
                            postImageUrl = post.postImageUrl,
                            authorNickname = post.authorLogin,
                            authorAvatar = post.authorIcon,
                            votesCount = post.votesCount,
                            bookmarksCount = post.bookmarksCount,
                            viewsCount = post.viewsCount,
                            commentsCount = post.commentsCount,
                            createdTimestamp = createdLabel,
                            content = postDetailsList,
                        )
                        _data.value = postView
                    }
                }
            } else {
                //todo show errors
            }
            withContext(Dispatchers.Main) {
                delay(500)
                _isLoading.value = false
            }
        }
    }
}