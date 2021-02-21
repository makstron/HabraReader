package com.klim.habrareader.data.repositories.postsThumb

import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.domain.UseCaseBase
import com.klim.habrareader.domain.entities.PostThumbEntity
import com.klim.habrareader.domain.repositories.PostThumbRepositoryI
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class PostThumbsRepository(
    private val scope: CoroutineScope,
    private var localDataSource: PostThumbsDataSourceI,
    private var remoteDataSource: PostThumbsDataSourceI
) : PostThumbRepositoryI {

    override suspend fun getAllPosts(
        flow: MutableSharedFlow<PostThumbEntity>,
        cached: Boolean,
        remote: Boolean,
        postsThreshold: PostsThreshold,
        onComplete: (UseCaseBase.CompleteStatus) -> Unit
    ) {

        scope.launch(Dispatchers.IO) {
            var localRequest: Deferred<Pair<Boolean, String>>? = null
            var remoteRequest: Deferred<Pair<Boolean, String>>? = null

            if (cached) {
                localRequest = scope.async(Dispatchers.IO) {
                    try {
                        val rawPosts = localDataSource.getAllPosts(postsThreshold)
                        rawPosts.collect { rawPost ->
                            flow.emit(PostDomainMapper.transform(rawPost, true))
                        }
                        Pair(true, "")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Pair(false, e.toString())
                    }
                }
            }

            if (remote) {
                remoteRequest = scope.async(Dispatchers.IO) {
                    try {
                        val rawPosts = remoteDataSource.getAllPosts(postsThreshold)
                        rawPosts.collect { rawPost ->
                            flow.emit(PostDomainMapper.transform(rawPost, false))
                        }
                        Pair(true, "")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Pair(false, e.toString())
                    }
                }
            }

            val resultLocal = localRequest?.await()
            val resultRemote = remoteRequest?.await()

            onComplete(UseCaseBase.CompleteStatus(resultLocal?.first == true && resultRemote?.first == true, null))
        }
    }

    override suspend fun getBestPosts(
        flow: MutableSharedFlow<PostThumbEntity>,
        cached: Boolean,
        remote: Boolean,
        postsPeriod: PostsPeriod,
        onComplete: (UseCaseBase.CompleteStatus) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
            var localRequest: Deferred<Pair<Boolean, String>>? = null
            var remoteRequest: Deferred<Pair<Boolean, String>>? = null

            if (cached) {
                localRequest = scope.async(Dispatchers.IO) {
                    try {
                        val rawPosts = localDataSource.getBestPosts(postsPeriod)
                        rawPosts.collect { rawPost ->
                            flow.emit(PostDomainMapper.transform(rawPost))
                        }
                        Pair(true, "")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Pair(false, e.toString())
                    }
                }
            }

            if (remote) {
                remoteRequest = scope.async(Dispatchers.IO) {
                    try {
                        remoteDataSource.getBestPosts(postsPeriod)
                            .catch { e ->
                                e.printStackTrace()
                            }
                            .collect { rawPost ->
                                flow.emit(PostDomainMapper.transform(rawPost, false))
                            }

                        Pair(true, "")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Pair(false, e.toString())
                    }
                }
            }

            val resultLocal = localRequest?.await()
            val resultRemote = remoteRequest?.await()

            onComplete(UseCaseBase.CompleteStatus(resultLocal?.first == true && resultRemote?.first == true, null))
        }
    }

}