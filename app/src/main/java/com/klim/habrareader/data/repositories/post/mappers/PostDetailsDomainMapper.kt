package com.klim.habrareader.data.repositories.post.mappers

import com.klim.habrareader.data.repositories.post.dtos.PostDetailsDTO
import com.klim.habrareader.domain.entities.PostDetailsEntity

fun PostDetailsDTO.map() = PostDetailsEntity(
    id = this.id,
    title = this.title,
    postImageUrl = this.postImage,
    description = this.description,
    createdTimestamp = this.createdTimestamp,
    votesCount = this.votesCount,
    bookmarksCount = this.bookmarksCount,
    viewsCount = this.viewsCount,
    commentsCount = this.commentsCount,
    authorLogin = this.authorLogin,
    authorIcon = this.authorImage
)

object PostDetailsDomainMapper {
    fun transform(postDetails: PostDetailsEntity): PostDetailsDTO {
        return PostDetailsDTO(
            id = postDetails.id,
            title = postDetails.title,
            description = postDetails.description,
            postImage = postDetails.postImageUrl,
            createdTimestamp = postDetails.createdTimestamp,
            votesCount = postDetails.votesCount,
            bookmarksCount = postDetails.bookmarksCount,
            viewsCount = postDetails.viewsCount,
            commentsCount = postDetails.commentsCount,
            authorLogin = postDetails.authorLogin,
            authorImage = postDetails.authorIcon
        )
    }

    fun transform(postDetailsDTO: PostDetailsDTO): PostDetailsEntity {
        return PostDetailsEntity(
            id = postDetailsDTO.id,
            title = postDetailsDTO.title,
            postImageUrl = postDetailsDTO.postImage,
            description = postDetailsDTO.description,
            createdTimestamp = postDetailsDTO.createdTimestamp,
            votesCount = postDetailsDTO.votesCount,
            bookmarksCount = postDetailsDTO.bookmarksCount,
            viewsCount = postDetailsDTO.viewsCount,
            commentsCount = postDetailsDTO.commentsCount,
            authorLogin = postDetailsDTO.authorLogin,
            authorIcon = postDetailsDTO.authorImage
        )
    }
}