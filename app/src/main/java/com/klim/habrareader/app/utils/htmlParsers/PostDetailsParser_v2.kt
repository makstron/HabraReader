package com.klim.habrareader.app.utils.htmlParsers

import com.klim.habrareader.app.windows.postDetails.entities.*
import com.klim.habrareader.app.windows.postDetails.enums.TitleSizesEnum
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import java.util.*

class PostDetailsParser_v2 {

    val listDetails = ArrayList<DetailBase>()
    var currentDetail: DetailBase? = null

    fun parse(body: String): ArrayList<DetailBase> {
        val doc = Jsoup.parse(body)
        val postTextDiv = doc.selectFirst("div.article-formatted-body")
            .child(0)
            .childNodes()

        parse(postTextDiv)

        return listDetails
    }

    //font

    private fun parse(nodesList: List<Node>) {

        for (node in nodesList) {
            when (node) {
                is Element -> {
                    when (node.tag().name) {
                        "h1", "h2", "h3", "h4", "h5", "h6" -> {
                            var textSize: TitleSizesEnum
                            when (node.tag().name) {
                                "h1" -> textSize = TitleSizesEnum.H1
                                "h2" -> textSize = TitleSizesEnum.H2
                                "h3" -> textSize = TitleSizesEnum.H3
                                "h4" -> textSize = TitleSizesEnum.H4
                                "h5" -> textSize = TitleSizesEnum.H5
                                "h6" -> textSize = TitleSizesEnum.H6
                                else -> textSize = TitleSizesEnum.H6
                            }
                            closeNode()
                            currentDetail = DetailTitle(node.text(), textSize)
                            closeNode()
                        }
                        "font" -> {
                            //todo check color
                            parse(node.childNodes())
                        }
                        "p" -> {
                            parse(node.childNodes())
                        }
                        "img" -> {
                            closeNode()
                            var imgSrc = node.attr("data-src")
                            if (imgSrc.isEmpty()) {
                                imgSrc = node.attr("src")
                            }
                            imgSrc = imgSrc.replace("habrastorage.org/", "hsto.org/")
                            currentDetail = DetailImage(imgSrc)
                            closeNode()
                        }
                        "figure" -> {
                            val img = node.selectFirst("img")
                            closeNode()
                            var imgSrc = img.attr("data-src")
                            if (imgSrc.isEmpty()) {
                                imgSrc = img.attr("src")
                            }
                            currentDetail = DetailImage(imgSrc)
//                            currentDetail = DetailImage(img.attr("src")) //small picture, should be loaded at first
                            closeNode()
                        }
                        "ul", "ol" -> {
                            val list = ArrayList<String>()
                            val listType = when (node.tag().name) {
                                "ul" -> DetailList.ListType.UNORDERED
                                "ol" -> DetailList.ListType.ORDERED
                                else -> DetailList.ListType.UNORDERED
                            }
                            node.select("li").forEach { li ->
                                list.add(li.text())
                            }
                            closeNode()
                            currentDetail = DetailList(listType, list)
                            closeNode()
                            //todo check text styles inside list item
                        }
                        "br" -> {
                            closeNode()
                            currentDetail = DetailEmpty()
                            closeNode()
                        }
                        "div" -> {
                            when {
                                //embeded
                                node.hasClass("oembed") -> {
                                    val iframe = node.select("iframe")
                                    val html = "<iframe width=\"100%\" height=\"auto\"\n" +
                                            "                                style=\"display: block; border-style:none; margin: 0 auto; max-width: 100% !important;\"\n" +
                                            "                                src=\"${iframe.attr("src")}\"></iframe>"
                                    closeNode()
                                    currentDetail = DetailEmbedded(html)
                                    closeNode()
                                }
                                node.hasClass("tm-iframe_temp") -> {
                                    val html = "<iframe width=\"100%\" height=\"auto\"\n" +
                                            "                                style=\"display: block; border-style:none; margin: 0 auto; max-width: 100% !important;\"\n" +
                                            "                                src=\"${node.attr("data-src")}\"></iframe>"
                                    closeNode()
                                    currentDetail = DetailEmbedded(html)
                                    closeNode()
                                }
                                node.hasClass("spoiler") -> {
                                    //todo
                                    closeNode()
                                    currentDetail = DetailUnknown("[SPOILER]\n" + node.selectFirst(".spoiler_title").text() + "\n" + node.selectFirst(".spoiler_text").text())
                                    closeNode()
                                }
                                else -> {
                                    parse(node.childNodes())
                                }
                            }
                        }
                        "table" -> {
                            closeNode()
                            currentDetail = DetailUnknown("[TABLE]\n" + node.text())
                            closeNode()
                        }
                        "a" -> {
                            //todo abb color
                            if (node.text().trim().isNotEmpty()) {
                                currentDetail?.let { curNode ->
                                    if (curNode.type == PostDetailType.TEXT) {
                                        (curNode as DetailText).content += node.text()
                                    }
                                } ?: run {
                                    closeNode()
                                    currentDetail = DetailText(node.text())
                                }
                            }
                        }
                        "strong" -> {
                            //todo abb color
                            if (node.text().trim().isNotEmpty()) {
                                currentDetail?.let { curNode ->
                                    if (curNode.type == PostDetailType.TEXT) {
                                        (curNode as DetailText).content += node.text()
                                    }
                                } ?: run {
                                    closeNode()
                                    currentDetail = DetailText(node.text())
                                }
                            }
                        }
                        "sup" -> {
                            //todo move to top
                            if (node.text().trim().isNotEmpty()) {
                                currentDetail?.let { curNode ->
                                    if (curNode.type == PostDetailType.TEXT) {
                                        (curNode as DetailText).content += "^${node.text()}"
                                    }
                                } ?: run {
                                    closeNode()
                                    currentDetail = DetailText("^${node.text()}")
                                }
                            }
                        }
                        "i" -> {
                            //todo need cursive in separate block
                            if (node.text().trim().isNotEmpty()) {
                                currentDetail?.let { curNode ->
                                    if (curNode.type == PostDetailType.TEXT) {
                                        (curNode as DetailText).content += node.text()
                                    }
                                } ?: run {
                                    closeNode()
                                    currentDetail = DetailText(node.text())
                                }
                            }
                        }
                        "code" -> {
                            //todo need code style in text
                            if (node.text().trim().isNotEmpty()) {
                                currentDetail?.let { curNode ->
                                    if (curNode.type == PostDetailType.TEXT) {
                                        (curNode as DetailText).content += "(${node.text()})"
                                    }
                                } ?: run {
                                    closeNode()
                                    currentDetail = DetailText("(${node.text()})")
                                }
                            }
                        }
                        "em" -> {
                            //todo need cursive style in text
                            if (node.text().trim().isNotEmpty()) {
                                currentDetail?.let { curNode ->
                                    if (curNode.type == PostDetailType.TEXT) {
                                        (curNode as DetailText).content += node.text()
                                    }
                                } ?: run {
                                    closeNode()
                                    currentDetail = DetailText(node.text())
                                }
                            }
                        }
                        "hr" -> {
                            closeNode()
                            currentDetail = DetailDivider()
                            closeNode()
                        }
                        "blockquote" -> {
                            closeNode()
                            currentDetail = DetailQuote(node.text())
                            closeNode()
                        }
                        "pre" -> {
                            closeNode()
                            currentDetail = DetailCode(node.text())
                            closeNode()
                        }
                        else -> {
                            closeNode()
                            currentDetail = DetailUnknown(node.text())
                            closeNode()
                        }
                    }
                }
                is TextNode -> {
                    if (node.text().trim().isNotEmpty()) {
                        currentDetail?.let { curNode ->
                            if (curNode.type == PostDetailType.TEXT) {
                                (curNode as DetailText).content += node.text()
                            }
                        } ?: run {
                            closeNode()
                            currentDetail = DetailText(node.text())
                        }
                    }
                }
            }
        }
    }

    private fun closeNode() {
        currentDetail?.let {
            listDetails.add(it)
            currentDetail = null
        }
    }
}