package com.klim.habrareader.data.repositories.post.dataSources.remote.parsers

import android.annotation.SuppressLint
import com.klim.habrareader.data.repositories.post.dtos.PostThumbDTO
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.parser.Tag
import org.jsoup.select.Elements
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object ListPostsParser {

    @SuppressLint("SimpleDateFormat")
    private val postDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    fun parse(body: String): List<PostThumbDTO> {
        val list = ArrayList<PostThumbDTO>()
        getRawArticles(body)
            .forEach { articleWrapper ->
                try {
                    val article = parseArticle(articleWrapper)
                    list.add(article)
                } catch (e: Exception) {
                    e.printStackTrace()
                    //todo do something with errors
                }
            }
        return list
    }

    private fun getRawArticles(body: String): Elements {
        return Jsoup.parse(body)
            .selectFirst("div.tm-articles-list")
            .select("article.tm-articles-list__item")
    }

    private fun parseArticle(articleWrapper: Element): PostThumbDTO {
        val postThumb = PostThumbDTO()

        setPostId(postThumb, articleWrapper)
        //article
        parseSnippet(postThumb, articleWrapper)
        parseFooter(postThumb, articleWrapper)

        return postThumb
    }

    private fun setPostId(postThumb: PostThumbDTO, articleWrapper: Element) {
        postThumb.id = articleWrapper.attr("id").toInt()
    }

    private fun parseSnippet(postThumb: PostThumbDTO, articleWrapper: Element) {
        val snippet = articleWrapper.selectFirst("div.tm-article-snippet")

        parseHeader(postThumb, snippet)
        parseTitle(postThumb, snippet)
        parseHubs(postThumb, snippet)
        parseLabels(postThumb, snippet)
        parseBody(postThumb, snippet)
    }

    private fun parseHeader(postThumb: PostThumbDTO, snippet: Element) {
        val header = snippet.selectFirst("div.tm-article-snippet__meta")

        parseAuthor(postThumb, header)
        parseTime(postThumb, header)
    }

    private fun parseAuthor(postThumb: PostThumbDTO, header: Element) {
        val authorWrapper = header.selectFirst(".tm-user-info.tm-article-snippet__author")

        postThumb.author = getAuthorNickname(authorWrapper)
        postThumb.authorIcon = getAuthorIcon(authorWrapper)
    }

    private fun getAuthorNickname(authorWrapper: Element) = authorWrapper.selectFirst(".tm-user-info__username").text().trim()

    private fun getAuthorIcon(authorWrapper: Element): String {
        val authorIconImg = authorWrapper.selectFirst("img")
        var authorIcon = ""
        if (authorIconImg != null) {
            authorIcon = authorIconImg.attr("src")
            if (authorIcon.indexOf("//") == 0) {
                authorIcon = "https:$authorIcon"
            }
        }
        return authorIcon
    }

    private fun parseTime(postThumb: PostThumbDTO, header: Element) {
        val timeRaw = header.selectFirst(".tm-article-snippet__datetime-published").selectFirst("time").attr("datetime")
        postDateTimeFormat.parse(timeRaw)?.let {
            postThumb.createdTimestamp = it.time
        }
    }

    private fun parseTitle(postThumb: PostThumbDTO, snippet: Element) {
        postThumb.title = snippet.selectFirst("h2.tm-article-snippet__title").text()
    }

    private fun parseHubs(postThumb: PostThumbDTO, snippet: Element) {
        //todo snippet.selectFirst("div.tm-article-snippet__hubs")
    }

    private fun parseLabels(postThumb: PostThumbDTO, snippet: Element) {
        //todo snippet.selectFirst("div.tm-article-snippet__labels")
    }

    private fun parseBody(postThumb: PostThumbDTO, snippet: Element) {
        val body = snippet.selectFirst("div.tm-article-body")

        parseDescription(postThumb, body)
        setDescriptionImage(postThumb, body)
    }

    //todo need refactor
    private fun parseDescription(postThumb: PostThumbDTO, body: Element) {
        val descriptionWrapper = body.selectFirst("div.article-formatted-body")

        val description = StringBuilder()
        descriptionWrapper.childNodes().forEach { node ->
            val tag = JSONObject()
            when (node) {
                is Element -> {
                    when (node.tag().name) {
                        "p" -> {
                            tag.put("tag", "p")
                            tag.put("text", node.text())

                            description.append(node.text().trim())
                            description.append("\n\n")
                        }
                        "br" -> {
                            tag.put("tag", "br")

                            description.append("\n")
                        }
                        "img" -> {
                        }
                        "ul" -> {
                            for (j in 0 until node.children().size) {
                                description.append("  • ")
                                description.append(node.children()[j].text().trim())
                                description.append("\n\n")
                            }
                        }
                        "ol" -> {
                            for (j in 0 until node.children().size) {
                                description.append("  ${j + 1} ")
                                description.append(node.children()[j].text().trim())
                                description.append("\n")
                            }
                        }
                        "a" -> {
                            val text = node.text().trim()
                            if (text.isNotEmpty()) {
                                description.append(text)
                            }
                        }
                        "i" -> {
                            description.append(node.text().trim())
                        }
                        "h1","h2","h3","h4","h5","h6" -> {
                            description.append(node.text().trim())
                            description.append("\n")
                        }
                        else -> {
                            description.append(node.tag().name + "******************\r\n")
                            description.append(node.text().trim())
                            description.append("******\n")
                        }
                    }
                }
                is TextNode -> {
                    description.append(node.text().trim())
                }
            }
        }
        postThumb.shortDescription = description.toString().trim()
    }

    private fun setDescriptionImage(postThumb: PostThumbDTO, body: Element) {
        val descriptionImg = body.selectFirst("img")
        descriptionImg?.let {
            postThumb.postImage = descriptionImg.attr("src")
        }
    }

    private fun parseFooter(postThumb: PostThumbDTO, articleWrapper: Element) {
        val footer = articleWrapper.selectFirst("div.tm-data-icons")

        parseRating(postThumb, footer)
        parseViewsCount(postThumb, footer)
        parseBookmarks(postThumb, footer)
        parseComments(postThumb, footer)
    }

    private fun parseRating(postThumb: PostThumbDTO, footer: Element) {
        postThumb.votesCount = footer.selectFirst(".tm-votes-meter").selectFirst(".tm-votes-meter__value").text().toInt()
    }

    private fun parseViewsCount(postThumb: PostThumbDTO, footer: Element) {
        postThumb.viewsCount = footer.selectFirst(".tm-icon-counter").selectFirst(".tm-icon-counter__value").text()
    }

    private fun parseBookmarks(postThumb: PostThumbDTO, footer: Element) {
        postThumb.bookmarksCount = footer.selectFirst(".bookmarks-button").selectFirst(".bookmarks-button__counter").text().toInt()
    }

    private fun parseComments(postThumb: PostThumbDTO, footer: Element) {
        val commentsSpan = footer.selectFirst(".tm-article-comments-counter-link").selectFirst(".tm-article-comments-counter-link__value")
        if (commentsSpan != null) {
            postThumb.commentsCount = commentsSpan.text().toInt() // todo can be not nomber ex(32.2k)
        }
    }
}