package com.klim.habrareader.data.repositories.post.dataSources.remote.parsers

import com.klim.habrareader.data.repositories.post.dtos.PostThumbDTO
import org.jsoup.Jsoup
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object ListPostsParser {

    //todo nee refactor and optimization
    fun parse(body: String): List<PostThumbDTO> {
        val list = ArrayList<PostThumbDTO>()

        val doc = Jsoup.parse(body)
        doc.selectFirst("div.tm-articles-list")
        .select("article.tm-articles-list__item").forEach { articleWrapper ->
            try {
                //id
                val id = articleWrapper.attr("id").toInt()
//                val id = idRaw.substring(idRaw.indexOf("_") + 1).toInt()

                //article
                val articleSnippet = articleWrapper.selectFirst("div.tm-article-snippet")
                //header
                val header = articleSnippet.selectFirst("div.tm-article-snippet__meta")
                val authorWrapper = header.selectFirst(".tm-user-info.tm-article-snippet__author")
                val author = authorWrapper.selectFirst(".tm-user-info__username").text().trim()
                val authorIconImg = authorWrapper.selectFirst("img")
                var authorIcon = ""
                if (authorIconImg != null) {
                    authorIcon = authorIconImg.attr("src")
                    if (authorIcon.indexOf("//") == 0) {
                        authorIcon = "https:" + authorIcon
                    }
                }
                val timeRaw = header.selectFirst(".tm-article-snippet__datetime-published").selectFirst("time").attr("datetime")
                val time = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(timeRaw).time
//                var startPart = ""
//                var endPart = ""
//                val cal = Calendar.getInstance();
//                cal.set(Calendar.HOUR, 0)
//                cal.set(Calendar.HOUR_OF_DAY, 0)
//                cal.set(Calendar.MINUTE, 0)
//                cal.set(Calendar.SECOND, 0)
//                cal.set(Calendar.MILLISECOND, 0)
//                if (timeRaw.contains("сегодня")) { //сегодня в 18:53
//                    val idx = timeRaw.lastIndexOf(" в ")
//                    startPart = timeRaw.substring(0, idx) // сегодня
//                    endPart = timeRaw.substring(idx + 3) // 18:53
//
//                    val format: DateFormat = SimpleDateFormat("HH:mm")
//                    val date = format.parse(endPart)
//
//                    cal.set(Calendar.HOUR_OF_DAY, date.hours)
////                            cal.set(Calendar.HOUR, date.hours)
//                    cal.set(Calendar.MINUTE, date.minutes)
//                } else if (timeRaw.contains("вчера")) { //вчера в 17:26
//                    val idx = timeRaw.lastIndexOf(" в ")
//                    startPart = timeRaw.substring(0, idx) // вчера
//                    endPart = timeRaw.substring(idx + 3) // 17:26
//
//                    cal.add(Calendar.DATE, -1)
//                    val format: DateFormat = SimpleDateFormat("HH:mm")
//                    val date = format.parse(endPart)
//
//                    //todo fix it for different between moscov and user timebelt
//                    if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 23) {
//                        cal.add(Calendar.DATE, 1)
//                    }
//
//                    cal.set(Calendar.HOUR_OF_DAY, date.hours)
////                            cal.set(Calendar.HOUR, date.hours)
//                    cal.set(Calendar.MINUTE, date.minutes)
//                } else { //27 декабря 2020 в 22:46 TODO can be trouble with it
//                    val idx = timeRaw.lastIndexOf(" в ")
//                    startPart = timeRaw.substring(0, idx) // 27 декабря 2020
//                    endPart = timeRaw.substring(idx + 3) // 22:46
//
////                    startPart.replace("января", "")
//
////                    val format: DateFormat = SimpleDateFormat("d MMMM yyyy")
////                    val date = format.parse(startPart)
//
//                    val russian = Locale("ru")
//                    val dfs: DateFormatSymbols = DateFormatSymbols.getInstance(russian)
//                    dfs.months = arrayOf("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря")
//                    val df = DateFormat.getDateInstance(DateFormat.LONG, russian)
//                    val sdf = df as SimpleDateFormat
//                    sdf.dateFormatSymbols = dfs
//
//                    val format = SimpleDateFormat("d MMMM yyyy")
//                    format.dateFormatSymbols = dfs
//
//                    val date = format.parse(startPart)
//
//
//                    cal.set(Calendar.DATE, date.date)
//                    cal.set(Calendar.DAY_OF_MONTH, date.day)
//                    cal.set(Calendar.MONTH, date.month)
//                    cal.set(Calendar.YEAR, date.year + 1900)
//
//                    val _format: DateFormat = SimpleDateFormat("HH:mm")
//                    val _date = _format.parse(endPart)
//
//                    cal.set(Calendar.HOUR_OF_DAY, _date.hours)
////                            cal.set(Calendar.HOUR, _date.hours)
//                    cal.set(Calendar.MINUTE, _date.minutes)
//                }
//                cal.add(Calendar.HOUR_OF_DAY, -3)
//                time = cal.timeInMillis

                //title
                val title = articleSnippet.selectFirst("h2.tm-article-snippet__title").text()

                //body
                val body = articleSnippet.selectFirst("div.tm-article-body")
                val descriptionDiv = body.selectFirst("div.article-formatted-body")
                val description = descriptionDiv.text()
                val descriptionImg = body.selectFirst("img.tm-article-snippet__lead-image")
                var postImage = ""
                descriptionImg?.let {
                    postImage = descriptionImg.attr("src")
                    if (authorIcon.indexOf("//") == 0) {
                        authorIcon = "https:" + authorIcon
                    }
                }

                //footer
                val footer = articleWrapper.selectFirst("div.tm-data-icons")
                val rating = footer.selectFirst(".tm-votes-meter").selectFirst(".tm-votes-meter__value")
                val votes = rating.text().toInt()
//                val votesTotal = rating.attr("title")
                val viewsCount = footer.selectFirst(".tm-icon-counter").selectFirst(".tm-icon-counter__value").text()
                val bookmarks = footer.selectFirst(".bookmarks-button").selectFirst(".bookmarks-button__counter").text().toInt()
                val commentsSpan = footer.selectFirst(".tm-article-comments-counter-link").selectFirst(".tm-article-comments-counter-link__value")
                var commentsCount = 0
                if (commentsSpan != null) {
                    commentsCount = commentsSpan.text().toInt() // todo can be not nomber ex(32.2k)
                }

                list.add(
                    PostThumbDTO(
                        id = id,
                        title = title,
                        shortDescription = description,
                        postImage = postImage,
                        createdTimestamp = time,
                        votesCount = votes,
                        bookmarksCount = bookmarks,
                        viewsCount = viewsCount,
                        commentsCount = commentsCount,
                        author = author,
                        authorIcon = authorIcon
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return list
    }
}