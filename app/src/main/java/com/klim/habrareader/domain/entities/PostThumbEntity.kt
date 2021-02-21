package com.klim.habrareader.domain.entities

data class PostThumbEntity(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val postImage: String?,
    val createdTimestamp: Long,
    val votesCount: Int,
    val bookmarksCount: Int,
    val viewsCount: String,
    val commentsCount: Int,

    val authorLogin: String,
    val authorIcon: String?,

    var isCashed: Boolean = true
)