package com.klim.habrareader.data.repositories.author

import com.klim.habrareader.data.repositories.author.dataSources.AuthorDataSourceI
import com.klim.habrareader.domain.entities.AuthorEntity
import com.klim.habrareader.domain.repositories.AuthorRepositoryI

class AuthorRepository(
    private var localDataSource: AuthorDataSourceI,
    private var remoteDataSource: AuthorDataSourceI
) : AuthorRepositoryI {
    override suspend fun getAuthor(cached: Boolean, remote: Boolean, authorLogin: String): AuthorEntity {
        TODO("Not yet implemented")
    }

    override fun saveAuthor(author: AuthorEntity): Boolean {
        localDataSource.saveAuthor(AuthorDomainMapper.transform(author))
        return true
    }

}