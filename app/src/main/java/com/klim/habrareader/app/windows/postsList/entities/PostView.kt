package com.klim.habrareader.app.windows.postsList.entities

data class PostView(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val postImage: String?,
    val createdTime: String,
    val createdTimestamp: Long,
    val votesCount: Int,
    val bookmarksCount: String,
    val viewsCount: String,
    val commentsCount: String,
    val author: String,
    val authorIcon: String?,
)