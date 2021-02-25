package com.klim.habrareader.app.windows.postDetails

import com.klim.habrareader.app.windows.postDetails.entities.DetailBase
import java.util.ArrayList

data class PostDetailsView(
    val title: String,
    val postImageUrl: String?,
    val authorNickname: String,
    val authorAvatar: String?,

    val votesCount: Int,
    val bookmarksCount: Int,
    val viewsCount: String,
    val commentsCount: Int,

    val createdTimestamp: String,

    val content: ArrayList<DetailBase>
)
