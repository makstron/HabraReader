package com.klim.habrareader.data.repositories.post

import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.data.repositories.post.mappers.PostDetailsDomainMapper
import com.klim.habrareader.data.repositories.post.mappers.PostThumbDomainMapper
import com.klim.habrareader.domain.UseCaseBase
import com.klim.habrareader.domain.entities.PostDetailsEntity
import com.klim.habrareader.domain.entities.PostThumbEntity
import com.klim.habrareader.domain.repositories.PostRepositoryI
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class PostRepository(
    private val scope: CoroutineScope,
    private var localDataSource: PostDataSourceI,
    private var remoteDataSource: PostDataSourceI
) : PostRepositoryI {

    override fun getAllPosts(
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
                            flow.emit(PostThumbDomainMapper.transform(rawPost, true))
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
                            flow.emit(PostThumbDomainMapper.transform(rawPost, false))
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

    override fun getBestPosts(
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
                            flow.emit(PostThumbDomainMapper.transform(rawPost))
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
                                flow.emit(PostThumbDomainMapper.transform(rawPost, false))
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

    override fun getPostDetails(cached: Boolean, remote: Boolean, postId: Int, onComplete: (UseCaseBase.CompleteStatus) -> Unit) {
        scope.launch(Dispatchers.IO) {
            var localRequest: Deferred<PostDetailsEntity?>? = null
            var remoteRequest: Deferred<PostDetailsEntity?>? = null

            if (cached) {
                localRequest = scope.async(Dispatchers.IO) {
                    val postDTO = localDataSource.getPost(postId)
                    var postEntity: PostDetailsEntity? = null
                    if (postDTO != null/* && !postDTO.description.isNullOrEmpty()*/) {
                        postEntity = PostDetailsDomainMapper.transform(postDTO)
                    }
                    postEntity
                }
            }

            if (remote) {
                remoteRequest = scope.async(Dispatchers.IO) {
                    val postDTO = remoteDataSource.getPost(postId)
                    var postEntity: PostDetailsEntity? = null
                    if (postDTO != null) {
                        postEntity = PostDetailsDomainMapper.transform(postDTO)
                    }
                    postEntity
                }
            }

            try {
                val resultLocal = localRequest?.await()
                onComplete(UseCaseBase.CompleteStatus(true, resultLocal))
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(UseCaseBase.CompleteStatus(false, e.toString()))
            }

            try {
                val resultRemote = remoteRequest?.await()
                onComplete(UseCaseBase.CompleteStatus(true, resultRemote))
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(UseCaseBase.CompleteStatus(true, e.toString()))
            }
        }
    }

    override fun savePostThumb(post: PostThumbEntity): Boolean {
        localDataSource.savePostThumb(PostThumbDomainMapper.transform(post))
        return true
    }

    override fun savePostDetails(postDetails: PostDetailsEntity): Boolean {
        localDataSource.savePostDetails(PostDetailsDomainMapper.transform(postDetails))
        return true
    }

}