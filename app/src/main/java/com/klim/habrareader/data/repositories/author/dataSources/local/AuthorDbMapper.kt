package com.klim.habrareader.data.repositories.author.dataSources.local

import com.klim.habrareader.data.db.tables.Author
import com.klim.habrareader.data.repositories.author.dataSources.AuthorDTO

object AuthorDbMapper {
    fun transform(author: Author): AuthorDTO {
        return AuthorDTO(
            login = author.nickname,
            photoUrl = author.authorIcon,
        )
    }

    fun transform(postsDTO: AuthorDTO): Author {
        return Author(
            nickname = postsDTO.login,
            authorIcon = postsDTO.photoUrl,
        )
    }
}