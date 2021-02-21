package com.klim.habrareader.data.repositories.postsThumb.dataSources.local.mappers

import com.klim.habrareader.data.db.tables.Post
import com.klim.habrareader.data.repositories.postsThumb.PostThumbDTO
import com.klim.habrareader.data.repositories.postsThumb.dataSources.local.entities.PostThumbWithAuthor

object PostThumbDbMapper {
    fun transform(post: PostThumbWithAuthor): PostThumbDTO {
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
            author = post.author,
            authorIcon = post.authorIcon
        )
    }

    fun transform(postsThumbDTO: PostThumbDTO): Post {
        return Post(
            id = postsThumbDTO.id,
            title = postsThumbDTO.title,
            shortDescription = postsThumbDTO.shortDescription,
            "",
            postImage = postsThumbDTO.postImage,
            createdTimestamp = postsThumbDTO.createdTimestamp,
            votesCount = postsThumbDTO.votesCount,
            bookmarksCount = postsThumbDTO.bookmarksCount,
            viewsCount = postsThumbDTO.viewsCount,
            commentsCount = postsThumbDTO.commentsCount,
            authorNickname = postsThumbDTO.author,
        )
    }
}