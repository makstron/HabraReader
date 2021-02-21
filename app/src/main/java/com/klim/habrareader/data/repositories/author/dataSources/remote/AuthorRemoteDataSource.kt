package com.klim.habrareader.data.repositories.author.dataSources.remote

import com.klim.habrareader.data.repositories.author.dataSources.AuthorDTO
import com.klim.habrareader.data.repositories.author.dataSources.AuthorDataSourceI
import com.klim.habrareader.data.retrofit.RetrofitProvider

class AuthorRemoteDataSource(var retrofit: RetrofitProvider) : AuthorDataSourceI {

    override fun getAuthor(authorLogin: String): AuthorDTO {
        TODO("Not yet implemented")
    }

    override fun saveAuthor(author: AuthorDTO): Boolean {
        throw UnsupportedOperationException("I can not do these")
    }

}


