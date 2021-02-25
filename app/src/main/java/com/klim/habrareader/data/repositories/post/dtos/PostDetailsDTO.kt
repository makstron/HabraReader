package com.klim.habrareader.data.repositories.post.dtos

data class PostDetailsDTO(
    val id: Int,
    val title: String,
    val description: String,
    val postImage: String?,
    val createdTimestamp: Long,
    val votesCount: Int,
    val bookmarksCount: Int,
    val viewsCount: String,
    val commentsCount: Int,
    val authorLogin: String,
    val authorImage: String?,
)
