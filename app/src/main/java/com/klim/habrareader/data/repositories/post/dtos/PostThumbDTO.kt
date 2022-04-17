package com.klim.habrareader.data.repositories.post.dtos

data class PostThumbDTO(
    var id: Int = 0,
    var title: String = "",
    var shortDescription: String = "",
    var postImage: String? = null,
    var createdTimestamp: Long = 0L,
    var votesCount: Int = 0,
    var bookmarksCount: Int = 0,
    var viewsCount: String = "",
    var commentsCount: Int = 0,
    var author: String = "",
    var authorIcon: String? = null,
)
