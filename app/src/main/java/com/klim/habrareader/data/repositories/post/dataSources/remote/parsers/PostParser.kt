package com.klim.habrareader.data.repositories.post.dataSources.remote.parsers

import com.klim.habrareader.data.repositories.post.dtos.PostDetailsDTO
import org.jsoup.Jsoup
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object PostParser {

    fun parse(body: String): PostDetailsDTO {
        val doc = Jsoup.parse(body)

        var postUrl = doc.selectFirst("link[rel=canonical]").attr("href")
        postUrl = postUrl.substring(0, postUrl.length - 1)
        val id = postUrl.substring(postUrl.lastIndexOf("/") + 1).toInt() // <link rel="canonical" href="https://habr.com/ru/post/243363/">

        val authorNickname = doc.selectFirst("span.user-info__nickname").text()
        var authorIcon: String? = null
        val authorIconDom = doc.selectFirst("img.user-info__image-pic")
        authorIconDom?.let {
            authorIcon = authorIconDom.attr("src")
            if (authorIcon!!.indexOf("//") == 0) {
                authorIcon = "https:" + authorIcon
            }
        }

        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val createdDate = format.parse(doc.selectFirst("span.post__time").attr("data-time_published").replace("T", " ").replace("Z", ""))
        val calendar = Calendar.getInstance()
        calendar.time = createdDate
//        calendar.add(Calendar.HOUR_OF_DAY, -3) //moscow timebelt
        val createdDateTime = calendar.timeInMillis

        val postTitle = doc.selectFirst("span.post__title-text").text()

        var postBodyDiv = doc.selectFirst("div.post__body_full").html()

        val votes = doc.selectFirst("span.voting-wjt__counter").text().replace("–", "-").toInt()
        val bookmark = doc.selectFirst("span.bookmark__counter").text().toInt()
        val views = doc.selectFirst("span.post-stats__views-count").text()
        val commentsView = doc.selectFirst("span.post-stats__comments-count")
        var comments = 0
        commentsView?.let {
            comments = doc.selectFirst("span.post-stats__comments-count").text().toInt()
        }


        return PostDetailsDTO(
            id = id,
            title = postTitle,
            description = postBodyDiv,
            postImage = null,
            createdTimestamp = createdDateTime,
            votesCount = votes,
            bookmarksCount = bookmark,
            viewsCount = views,
            commentsCount = comments,
            authorLogin = authorNickname,
            authorImage = authorIcon,
        )
    }
}