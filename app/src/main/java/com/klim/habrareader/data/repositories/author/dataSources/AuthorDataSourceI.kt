package com.klim.habrareader.data.repositories.author.dataSources

interface AuthorDataSourceI {
    fun getAuthor(authorLogin: String): AuthorDTO

    fun saveAuthor(author: AuthorDTO): Boolean
}