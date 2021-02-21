package com.klim.habrareader.domain.repositories

import com.klim.habrareader.domain.entities.AuthorEntity

interface AuthorRepositoryI {

    suspend fun getAuthor(
        cached: Boolean = false,
        remote: Boolean = false,
        authorLogin: String
    ): AuthorEntity

    fun saveAuthor(post: AuthorEntity): Boolean
}