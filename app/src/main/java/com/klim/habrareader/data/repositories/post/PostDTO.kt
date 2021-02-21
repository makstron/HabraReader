package com.klim.habrareader.data.repositories.post

data class PostDTO(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val description: String,
    val postImage: String?,
    val createdTimestamp: Long,
    val votesCount: Int,
    val bookmarksCount: Int,
    val viewsCount: String,
    val commentsCount: Int,
    val authorLogin: String,
)
