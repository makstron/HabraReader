package com.klim.habrareader.data.repositories.post.dtos

data class PostThumbDTO(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val postImage: String?,
    val createdTimestamp: Long,
    val votesCount: Int,
    val bookmarksCount: Int,
    val viewsCount: String,
    val commentsCount: Int,
    val author: String,
    val authorIcon: String?,
)
