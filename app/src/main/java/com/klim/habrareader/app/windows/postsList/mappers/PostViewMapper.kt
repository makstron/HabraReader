package com.klim.habrareader.app.windows.postsList.mappers

import android.content.Context
import com.klim.habrareader.R
import com.klim.habrareader.app.windows.postsList.entities.PostView
import com.klim.habrareader.domain.entities.PostThumbEntity
import java.text.DateFormat
import java.util.*

object PostViewMapper {
    fun transform(
        context: Context,
        formatTime: DateFormat,
        formatDateTime: DateFormat,
        postThumb: PostThumbEntity
    ): PostView {
        //todo refactor it
        val calToday = Calendar.getInstance()
        val calYesterday = Calendar.getInstance()
        calYesterday.add(Calendar.DATE, -1)
        val calPost = Calendar.getInstance()
        calPost.timeInMillis = postThumb.createdTimestamp
        calPost.add(Calendar.HOUR_OF_DAY, 2) // 2 - is current timebelt

        val createdLabel: String
        when {
            calPost.get(Calendar.DAY_OF_MONTH) == calToday.get(Calendar.DAY_OF_MONTH) -> {
                createdLabel = "${context.getString(R.string.post_time_today)} ${context.getString(R.string.post_time_at)} ${formatTime.format(calPost.time)}"
            }
            calPost.get(Calendar.DAY_OF_MONTH) == calYesterday.get(Calendar.DAY_OF_MONTH) -> {
                createdLabel = "${context.getString(R.string.post_time_yesterday)} ${context.getString(R.string.post_time_at)} ${formatTime.format(calPost.time)}"
            }
            else -> {
                createdLabel = formatDateTime.format(calPost.time)
            }
        }

        return PostView(
            id = postThumb.id,
            title = postThumb.title,
            shortDescription = postThumb.shortDescription,
            postImage = postThumb.postImage,
            createdTime = createdLabel,
            createdTimestamp = postThumb.createdTimestamp,
            votesCount = postThumb.votesCount,
            bookmarksCount = postThumb.bookmarksCount.toString(),
            viewsCount = postThumb.viewsCount,
            commentsCount = postThumb.commentsCount.toString(),
            author = postThumb.authorLogin,
            authorIcon = postThumb.authorIcon,
        )
    }
}