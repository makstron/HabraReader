package com.klim.habrareader.domain.useCases

import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.domain.UseCaseBase
import com.klim.habrareader.domain.entities.AuthorEntity
import com.klim.habrareader.domain.entities.PostEntity
import com.klim.habrareader.domain.entities.PostThumbEntity
import com.klim.habrareader.domain.repositories.AuthorRepositoryI
import com.klim.habrareader.domain.repositories.PostRepositoryI
import com.klim.habrareader.domain.repositories.PostThumbRepositoryI
import com.klim.habrareader.domain.useCaseInterfaces.PostThumbsUseCaseI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class PostThumbsUseCase(
    private val flow: MutableSharedFlow<PostThumbEntity>,
    private val scope: CoroutineScope,
    private val postThumbsRepository: PostThumbRepositoryI,
    private val postRepository: PostRepositoryI,
    private val authorRepository: AuthorRepositoryI
) : UseCaseBase(), PostThumbsUseCaseI {

    init {
        //save posts and authors in cache
        scope.launch(Dispatchers.IO) {
            flow.collect { postThumbEntity ->
                if (!postThumbEntity.isCashed) {
                    val post = PostEntity(
                        id = postThumbEntity.id,
                        title = postThumbEntity.title,
                        postImageUrl = postThumbEntity.postImage,
                        shortDescription = postThumbEntity.shortDescription,
                        createdTimestamp = postThumbEntity.createdTimestamp,
                        votesCount = postThumbEntity.votesCount,
                        bookmarksCount = postThumbEntity.bookmarksCount,
                        viewsCount = postThumbEntity.viewsCount,
                        commentsCount = postThumbEntity.commentsCount,
                        authorLogin = postThumbEntity.authorLogin,
                    )
                    postRepository.savePost(post)

                    val authorEntity = AuthorEntity(
                        authorLogin = postThumbEntity.authorLogin,
                        authorIcon = postThumbEntity.authorIcon,
                    )
                    authorRepository.saveAuthor(authorEntity)

                    println("POST ${postThumbEntity.title}   SAVED")
                }
            }
        }
    }

    override suspend fun getAllPosts(
        cached: Boolean,
        remote: Boolean,
        postsThreshold: PostsThreshold,
        onComplete: (CompleteStatus) -> Unit
    ) {
        postThumbsRepository.getAllPosts(flow, cached, remote, postsThreshold, onComplete)
    }

    override suspend fun getBestPosts(
        cached: Boolean,
        remote: Boolean,
        postsPeriod: PostsPeriod,
        onComplete: (CompleteStatus) -> Unit
    ) {
        postThumbsRepository.getBestPosts(flow, cached, remote, postsPeriod, onComplete)
    }

}