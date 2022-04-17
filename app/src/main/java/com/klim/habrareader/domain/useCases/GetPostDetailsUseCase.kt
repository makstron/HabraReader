package com.klim.habrareader.domain.useCases

import com.klim.habrareader.domain.Response
import com.klim.habrareader.domain.UseCaseBase
import com.klim.habrareader.domain.entities.PostDetailsEntity
import com.klim.habrareader.domain.repositories.PostRepositoryI
import com.klim.habrareader.domain.useCaseInterfaces.GetPostDetailsUseCaseI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class GetPostDetailsUseCase(val scope: CoroutineScope, private val repository: PostRepositoryI) : UseCaseBase(), GetPostDetailsUseCaseI {

    override fun getPostDetail(
        cached: Boolean,
        remote: Boolean,
        id: Int,
        onComplete: (CompleteStatus) -> Unit
    ) {

            repository.getPostDetails(cached, remote, id) { status ->
                //todo check if it is from remote
                if (status.isSuccessful) {
                    status.data?.let {
                        scope.launch(Dispatchers.IO) {
                            repository.savePostDetails(status.data as PostDetailsEntity)
                        }
                    }
                }
                onComplete(status)
            }
    }

    fun getPostDetail(
        cached: Boolean,
        remote: Boolean,
        id: Int,
    ): Flow<Response<PostDetailsEntity>> {

        return repository.getPostDetails(cached, remote, id)
    }



}