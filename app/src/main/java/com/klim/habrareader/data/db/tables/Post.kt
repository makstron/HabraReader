package com.klim.habrareader.data.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
class Post(
    @PrimaryKey
    var id: Int,

    var title: String,

    @ColumnInfo(name = "short_description")
    var shortDescription: String,

    var description: String,

    @ColumnInfo(name = "post_image")
    var postImage: String?,

    @ColumnInfo(name = "created_timestamp")
    var createdTimestamp: Long,

    @ColumnInfo(name = "votes_count")
    var votesCount: Int,

    @ColumnInfo(name = "bookmarks_count")
    var bookmarksCount: Int,

    @ColumnInfo(name = "views_count")
    var viewsCount: String,

    @ColumnInfo(name = "comments_count")
    var commentsCount: Int,

    @ColumnInfo(name = "author_nickname")
    var authorNickname: String,
) {

}