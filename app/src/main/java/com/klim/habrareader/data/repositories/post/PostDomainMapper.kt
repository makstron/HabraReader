package com.klim.habrareader.data.repositories.post

import com.klim.habrareader.domain.entities.PostEntity

object PostDomainMapper {
    fun transform(post: PostEntity): PostDTO {
        return PostDTO(
            id = post.id,
            title = post.title,
            shortDescription = post.shortDescription,
            description = post.description,
            postImage = post.postImageUrl,
            createdTimestamp = post.createdTimestamp,
            votesCount = post.votesCount,
            bookmarksCount = post.bookmarksCount,
            viewsCount = post.viewsCount,
            commentsCount = post.commentsCount,
            authorLogin = post.authorLogin,
        )
    }

    fun transform(postsThumbDTO: PostDTO): PostEntity {
        TODO("Not yet implemented")
    }
}