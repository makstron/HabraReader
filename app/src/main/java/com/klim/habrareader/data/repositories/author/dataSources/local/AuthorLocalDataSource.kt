package com.klim.habrareader.data.repositories.author.dataSources.local

import com.klim.habrareader.data.db.AppDatabase
import com.klim.habrareader.data.repositories.author.dataSources.AuthorDTO
import com.klim.habrareader.data.repositories.author.dataSources.AuthorDataSourceI

class AuthorLocalDataSource(val db: AppDatabase) : AuthorDataSourceI {

    override fun getAuthor(authorLogin: String): AuthorDTO {
        return AuthorDbMapper.transform(db.authorDao.getAuthor(authorLogin))
    }

    override fun saveAuthor(author: AuthorDTO): Boolean {
        db.authorDao.add(AuthorDbMapper.transform(author))
        return true
    }


}


