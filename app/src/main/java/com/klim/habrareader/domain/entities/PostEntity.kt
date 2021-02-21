package com.klim.habrareader.domain.entities

data class PostEntity(
    val id: Int,
    val title: String,
    val postImageUrl: String? = null,
    val shortDescription: String = "",
    val description: String = "",
    val createdTimestamp: Long,
    val votesCount: Int,
    val bookmarksCount: Int,
    val viewsCount: String,
    val commentsCount: Int,
    val authorLogin: String,
)