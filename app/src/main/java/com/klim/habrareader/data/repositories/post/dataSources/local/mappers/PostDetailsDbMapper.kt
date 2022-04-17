package com.klim.habrareader.data.repositories.post.dataSources.local.mappers

import com.klim.habrareader.data.db.tables.Post
import com.klim.habrareader.data.repositories.post.dataSources.local.models.PostDetailsWithAuthor
import com.klim.habrareader.data.repositories.post.dtos.PostDetailsDTO

fun PostDetailsWithAuthor.map() = PostDetailsDTO(
    id = this.id,
    title = this.title,
    description = this.description ?: "",
    postImage = this.postImage,
    createdTimestamp = this.createdTimestamp,
    votesCount = this.votesCount,
    bookmarksCount = this.bookmarksCount,
    viewsCount = this.viewsCount,
    commentsCount = this.commentsCount,
    authorLogin = this.author,
    authorImage = this.authorIcon
)

object PostDetailsDbMapper {
    fun transform(post: PostDetailsWithAuthor?): PostDetailsDTO? {
        if (post == null)
            return null
        return PostDetailsDTO(
            id = post.id,
            title = post.title,
            description = post.description ?: "",
            postImage = post.postImage,
            createdTimestamp = post.createdTimestamp,
            votesCount = post.votesCount,
            bookmarksCount = post.bookmarksCount,
            viewsCount = post.viewsCount,
            commentsCount = post.commentsCount,
            authorLogin = post.author,
            authorImage = post.authorIcon
        )
    }

    fun transform(postsDetailsDTO: PostDetailsDTO, shortDescription: String = "", postImage: String?): Post {
        return Post(
            id = postsDetailsDTO.id,
            title = postsDetailsDTO.title,
            postImage = postImage,
            shortDescription = shortDescription,
            description = postsDetailsDTO.description,
            createdTimestamp = postsDetailsDTO.createdTimestamp,
            votesCount = postsDetailsDTO.votesCount,
            bookmarksCount = postsDetailsDTO.bookmarksCount,
            viewsCount = postsDetailsDTO.viewsCount,
            commentsCount = postsDetailsDTO.commentsCount,
            authorNickname = postsDetailsDTO.authorLogin,
        )
    }
}