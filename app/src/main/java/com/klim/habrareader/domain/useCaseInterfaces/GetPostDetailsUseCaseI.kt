package com.klim.habrareader.domain.useCaseInterfaces

import com.klim.habrareader.domain.UseCaseBase

interface GetPostDetailsUseCaseI {
    fun getPostDetail(cached: Boolean, remote: Boolean, id: Int, onComplete: (UseCaseBase.CompleteStatus) -> Unit)
}