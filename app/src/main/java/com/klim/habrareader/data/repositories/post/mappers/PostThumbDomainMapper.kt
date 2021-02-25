package com.klim.habrareader.data.repositories.post.mappers

import com.klim.habrareader.data.repositories.post.dtos.PostThumbDTO
import com.klim.habrareader.domain.entities.PostThumbEntity

object PostThumbDomainMapper {
    fun transform(post: PostThumbEntity): PostThumbDTO {
        return PostThumbDTO(
            id = post.id,
            title = post.title,
            shortDescription = post.shortDescription,
            postImage = post.postImage,
            createdTimestamp = post.createdTimestamp,
            votesCount = post.votesCount,
            bookmarksCount = post.bookmarksCount,
            viewsCount = post.viewsCount,
            commentsCount = post.commentsCount,
            author = post.authorLogin,
            authorIcon = post.authorIcon
        )
    }

    fun transform(postsThumbDTO: PostThumbDTO, isCashed: Boolean = true): PostThumbEntity {
        return PostThumbEntity(
            id = postsThumbDTO.id,
            title = postsThumbDTO.title,
            shortDescription = postsThumbDTO.shortDescription,
            postImage = postsThumbDTO.postImage,
            createdTimestamp = postsThumbDTO.createdTimestamp,
            votesCount = postsThumbDTO.votesCount,
            bookmarksCount = postsThumbDTO.bookmarksCount,
            viewsCount = postsThumbDTO.viewsCount,
            commentsCount = postsThumbDTO.commentsCount,
            authorLogin = postsThumbDTO.author,
            authorIcon = postsThumbDTO.authorIcon,
            isCashed = isCashed
        )
    }
}