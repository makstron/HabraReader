package com.klim.habrareader.data.repositories.postsThumb.dataSources.local.entities

import androidx.room.ColumnInfo

class PostThumbWithAuthor(
    val id: Int,
    val title: String,
    @ColumnInfo(name = "post_image")
    val postImage: String?,
    @ColumnInfo(name = "short_description")
    val shortDescription: String,
    @ColumnInfo(name = "created_timestamp")
    val createdTimestamp: Long,
    @ColumnInfo(name = "votes_count")
    val votesCount: Int,
    @ColumnInfo(name = "bookmarks_count")
    val bookmarksCount: Int,
    @ColumnInfo(name = "views_count")
    val viewsCount: String,
    @ColumnInfo(name = "comments_count")
    val commentsCount: Int,
    @ColumnInfo(name = "author_nickname")
    val author: String,
    @ColumnInfo(name = "author_icon")
    val authorIcon: String,
)