package com.klim.habrareader.data.repositories.author

import com.klim.habrareader.data.repositories.author.dataSources.AuthorDTO
import com.klim.habrareader.domain.entities.AuthorEntity

object AuthorDomainMapper {
    fun transform(author: AuthorEntity): AuthorDTO {
        return AuthorDTO(
            login = author.authorLogin,
            photoUrl = author.authorIcon,
        )
    }
}