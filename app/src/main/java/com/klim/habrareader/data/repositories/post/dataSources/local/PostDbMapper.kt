package com.klim.habrareader.data.repositories.post.dataSources.local

import com.klim.habrareader.data.db.tables.Post
import com.klim.habrareader.data.repositories.post.PostDTO

object PostDbMapper {
    fun transform(post: Post): PostDTO {
        return PostDTO(
            id = post.id,
            title = post.title,
            shortDescription = post.shortDescription,
            description = post.description,
            postImage = post.postImage,
            createdTimestamp = post.createdTimestamp,
            votesCount = post.votesCount,
            bookmarksCount = post.bookmarksCount,
            viewsCount = post.viewsCount,
            commentsCount = post.commentsCount,
            authorLogin = post.authorNickname,
        )
    }

    fun transform(postsDTO: PostDTO): Post {
        return Post(
            id = postsDTO.id,
            title = postsDTO.title,
            postImage = postsDTO.postImage,
            shortDescription = postsDTO.shortDescription,
            description = postsDTO.description,
            createdTimestamp = postsDTO.createdTimestamp,
            votesCount = postsDTO.votesCount,
            bookmarksCount = postsDTO.bookmarksCount,
            viewsCount = postsDTO.viewsCount,
            commentsCount = postsDTO.commentsCount,
            authorNickname = postsDTO.authorLogin,
        )
    }
}