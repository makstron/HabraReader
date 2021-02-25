package com.klim.habrareader.data.repositories.post.dataSources.remote.parsers

import com.klim.habrareader.data.repositories.post.dtos.PostThumbDTO
import org.jsoup.Jsoup
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

object ListPostsParser {

    //todo nee refactor and optimization
    fun parse(body: String): List<PostThumbDTO> {
        val list = ArrayList<PostThumbDTO>()

        val doc = Jsoup.parse(body)
        doc.select("li.content-list__item_post[id~=post]").forEach { articleWrapper ->
            try {
                //id
                val idRaw = articleWrapper.attr("id")
                val id = idRaw.substring(idRaw.indexOf("_") + 1).toInt()

                //article
                val article = articleWrapper.selectFirst("article")
                //header
                val header = article.selectFirst("header")
                val authorWrapper = header.selectFirst(".post__user-info")
                val author = authorWrapper.selectFirst(".user-info__nickname").text()
                val authorIconImg = authorWrapper.selectFirst("img")
                var authorIcon = ""
                if (authorIconImg != null) {
                    authorIcon = authorIconImg.attr("src")
                    if (authorIcon.indexOf("//") == 0) {
                        authorIcon = "https:" + authorIcon
                    }
                }
                val timeRaw = header.selectFirst(".post__time").text()
                var time = System.currentTimeMillis()
                var startPart = ""
                var endPart = ""
                val cal = Calendar.getInstance();
                cal.set(Calendar.HOUR, 0)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                if (timeRaw.contains("сегодня")) { //сегодня в 18:53
                    val idx = timeRaw.lastIndexOf(" в ")
                    startPart = timeRaw.substring(0, idx) // сегодня
                    endPart = timeRaw.substring(idx + 3) // 18:53

                    val format: DateFormat = SimpleDateFormat("HH:mm")
                    val date = format.parse(endPart)

                    cal.set(Calendar.HOUR_OF_DAY, date.hours)
//                            cal.set(Calendar.HOUR, date.hours)
                    cal.set(Calendar.MINUTE, date.minutes)
                } else if (timeRaw.contains("вчера")) { //вчера в 17:26
                    val idx = timeRaw.lastIndexOf(" в ")
                    startPart = timeRaw.substring(0, idx) // вчера
                    endPart = timeRaw.substring(idx + 3) // 17:26

                    cal.add(Calendar.DATE, -1)
                    val format: DateFormat = SimpleDateFormat("HH:mm")
                    val date = format.parse(endPart)

                    //todo fix it for different between moscov and user timebelt
                    if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 23) {
                        cal.add(Calendar.DATE, 1)
                    }

                    cal.set(Calendar.HOUR_OF_DAY, date.hours)
//                            cal.set(Calendar.HOUR, date.hours)
                    cal.set(Calendar.MINUTE, date.minutes)
                } else { //27 декабря 2020 в 22:46 TODO can be trouble with it
                    val idx = timeRaw.lastIndexOf(" в ")
                    startPart = timeRaw.substring(0, idx) // 27 декабря 2020
                    endPart = timeRaw.substring(idx + 3) // 22:46

//                    startPart.replace("января", "")

//                    val format: DateFormat = SimpleDateFormat("d MMMM yyyy")
//                    val date = format.parse(startPart)

                    val russian = Locale("ru")
                    val dfs: DateFormatSymbols = DateFormatSymbols.getInstance(russian)
                    dfs.months = arrayOf("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря")
                    val df = DateFormat.getDateInstance(DateFormat.LONG, russian)
                    val sdf = df as SimpleDateFormat
                    sdf.dateFormatSymbols = dfs

                    val format = SimpleDateFormat("d MMMM yyyy")
                    format.dateFormatSymbols = dfs

                    val date = format.parse(startPart)


                    cal.set(Calendar.DATE, date.date)
                    cal.set(Calendar.DAY_OF_MONTH, date.day)
                    cal.set(Calendar.MONTH, date.month)
                    cal.set(Calendar.YEAR, date.year + 1900)

                    val _format: DateFormat = SimpleDateFormat("HH:mm")
                    val _date = _format.parse(endPart)

                    cal.set(Calendar.HOUR_OF_DAY, _date.hours)
//                            cal.set(Calendar.HOUR, _date.hours)
                    cal.set(Calendar.MINUTE, _date.minutes)
                }
                cal.add(Calendar.HOUR_OF_DAY, -3)
                time = cal.timeInMillis

                //title
                val title = article.selectFirst(".post__title").text()

                //body
                val body = article.selectFirst(".post__body")
                val descriptionDiv = body.selectFirst(".post__text")
                val description = descriptionDiv.text()
                val descriptionImg = body.selectFirst("img")
                var postImage = ""
                descriptionImg?.let {
                    postImage = descriptionImg.attr("src")
                    if (authorIcon.indexOf("//") == 0) {
                        authorIcon = "https:" + authorIcon
                    }
                }

                //footer
                val footer = article.selectFirst("footer")
                val rating = footer.selectFirst(".post-stats__result-counter")
                val votes = rating.text().replace("–", "-").toInt()
                val votesTotal = rating.attr("title")
                val bookmarks = footer.selectFirst(".bookmark__counter").text().toInt()
                val viewsCount = footer.selectFirst(".post-stats__views-count").text()
                val commentsSpan = footer.selectFirst(".post-stats__comments-count")
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