package com.klim.habrareader.app.windows.postsList

import android.app.Application
import androidx.lifecycle.*
import com.klim.habrareader.app.windows.postsList.entities.PostView
import com.klim.habrareader.app.windows.postsList.entities.UpdateListCommand
import com.klim.habrareader.app.windows.postsList.enums.Commands
import com.klim.habrareader.app.windows.postsList.filters.PostListType
import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.app.windows.postsList.mappers.PostViewMapper
import com.klim.habrareader.data.db.DbHelper
import com.klim.habrareader.data.repositories.author.AuthorRepository
import com.klim.habrareader.data.repositories.author.dataSources.local.AuthorLocalDataSource
import com.klim.habrareader.data.repositories.author.dataSources.remote.AuthorRemoteDataSource
import com.klim.habrareader.data.repositories.post.PostRepository
import com.klim.habrareader.data.repositories.post.dataSources.local.PostLocalDataSource
import com.klim.habrareader.data.repositories.post.dataSources.remote.PostRemoteDataSource
import com.klim.habrareader.data.repositories.postsThumb.PostThumbsRepository
import com.klim.habrareader.data.repositories.postsThumb.dataSources.local.PostThumbsLocalDataSource
import com.klim.habrareader.data.repositories.postsThumb.dataSources.remote.PostThumbsRemoteDataSource
import com.klim.habrareader.data.retrofit.RetrofitProvider
import com.klim.habrareader.domain.UseCaseBase
import com.klim.habrareader.domain.entities.PostThumbEntity
import com.klim.habrareader.domain.useCaseInterfaces.PostThumbsUseCaseI
import com.klim.habrareader.domain.useCases.PostThumbsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostsFragmentVM(val appContext: Application) : AndroidViewModel(appContext) {
    private val formatTime: DateFormat = SimpleDateFormat("HH:mm")
    private val formatDateTime: DateFormat = SimpleDateFormat("d MMMM yyyy в HH:mm")

    //flow for receive data
    private val flow = MutableSharedFlow<PostThumbEntity>(replay = 0)

    private val postThumbsUseCase: PostThumbsUseCaseI = PostThumbsUseCase(
        flow,
        viewModelScope,
        PostThumbsRepository(viewModelScope, PostThumbsLocalDataSource(DbHelper.get()), PostThumbsRemoteDataSource(RetrofitProvider())),
        PostRepository(PostLocalDataSource(DbHelper.get()), PostRemoteDataSource(RetrofitProvider())),
        AuthorRepository(AuthorLocalDataSource(DbHelper.get()), AuthorRemoteDataSource(RetrofitProvider()))
    )

    private val rawData = ArrayList<PostThumbEntity>()
    val data = ArrayList<PostView>()

    //selected filters
    private var listType: PostListType? = null
    private var postsThreshold: PostsThreshold? = null
    private var postsPeriod: PostsPeriod? = null

    //ui observable
    private val _lastChangedItem = MutableLiveData<UpdateListCommand>()
    val lastChangedItem: LiveData<UpdateListCommand> = _lastChangedItem

    private var _isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading


    init {
        viewModelScope.launch(Dispatchers.Default) {
            flow.collect { postThumbEntity ->
                var posToChange = -1
                var command = Commands.DO_NOTHING
                for (i in 0 until rawData.size) {
                    if (rawData[i].id == postThumbEntity.id) {
                        //todo add check by values
                        posToChange = i
                        break
                    }
                }
                if (posToChange != -1) {
                    if (!postThumbEntity.isCashed) {
                        rawData.set(posToChange, postThumbEntity)
                        data.set(posToChange, PostViewMapper.transform(appContext, formatTime, formatDateTime, postThumbEntity))
                        command = Commands.ITEM_CHANGED
                    }
                } else {
                    posToChange = 0
                    rawData.forEach {
                        when (listType) {
                            PostListType.ALL_POSTS -> {
                                if (it.createdTimestamp <= postThumbEntity.createdTimestamp) {
                                    return@forEach
                                }
                            }
                            PostListType.BEST_POSTS -> {
                                if (it.votesCount <= postThumbEntity.votesCount) {
                                    return@forEach
                                }
                            }
                        }
                        posToChange++
                    }
                    rawData.add(posToChange, postThumbEntity)
                    data.add(posToChange, PostViewMapper.transform(appContext, formatTime, formatDateTime, postThumbEntity))
                    command = Commands.ITEM_INSERTED
                }

                //update item in ui
                withContext(Dispatchers.Main) {
                    _lastChangedItem.value = UpdateListCommand(command, posToChange)
                }
            }
        }
    }

    //remove all inappropriate items after finish receiving data
    private fun loadingComplete(status: UseCaseBase.CompleteStatus) {
        if (status.isSuccessful) {
            viewModelScope.launch(Dispatchers.Default) {
                val rawDataIterator = rawData.iterator()
                val dataIterator = data.iterator()
                while (rawDataIterator.hasNext() && dataIterator.hasNext()) {
                    val rawItem = rawDataIterator.next()
                    val item = dataIterator.next()

                    if (rawItem.isCashed) {
                        var changedPosition = 0
                        for (i in 0 until rawData.size) {
                            if (rawData[i].id == rawItem.id) {
                                changedPosition = i
                                break
                            }
                        }

                        rawDataIterator.remove()
                        dataIterator.remove()

                        withContext(Dispatchers.Main) {
                            _lastChangedItem.value = UpdateListCommand(Commands.ITEM_REMOVED, changedPosition)
                        }
                    }
                }
            }
        } else {
            //todo show message
        }

        viewModelScope.launch(Dispatchers.Main) {
            _isLoading.value = false
        }
    }

    fun updatePostList(showProgress: Boolean, listType: PostListType, postsThreshold: PostsThreshold, postsPeriod: PostsPeriod, forceUpdate: Boolean = false) {
        if (forceUpdate || this.listType != listType || this.postsThreshold != postsThreshold || this.postsPeriod != postsPeriod) {
            this.listType = listType
            this.postsThreshold = postsThreshold
            this.postsPeriod = postsPeriod

            _isLoading.value = showProgress


            if (forceUpdate) {
                rawData.forEach {
                    it.isCashed = true
                }
            } else {
                rawData.clear()
                data.clear()
                _lastChangedItem.value = UpdateListCommand(Commands.UPDATE_All, 0)
            }

            viewModelScope.launch(Dispatchers.IO) {
                when (listType._id) {
                    PostListType.ALL_POSTS._id -> {
                        postThumbsUseCase.getAllPosts(cached = true, remote = true, postsThreshold = postsThreshold, onComplete = ::loadingComplete)
                    }
                    PostListType.BEST_POSTS._id -> {
                        postThumbsUseCase.getBestPosts(cached = true, remote = true, postsPeriod = postsPeriod, onComplete = ::loadingComplete)
                    }
                }
            }
        }
    }
}