package com.klim.habrareader.app.windows.postDetails

import com.klim.habrareader.app.windows.postDetails.entities.*
import com.klim.habrareader.app.windows.postDetails.enums.TitleSizesEnum
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import java.util.*

object PostDetailsParser {

    fun parse(body: String): ArrayList<DetailBase> {
        val doc = Jsoup.parse(body)
        val postTextDiv = doc.selectFirst("div.post__text")

        val listDetails = ArrayList<DetailBase>()
        var lastDetail: DetailBase
        for (node in postTextDiv.childNodes()) {
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
                            lastDetail = DetailTitle(node.text(), textSize)
                            listDetails.add(lastDetail)
                        }
                        "p" -> {
                            //todo make something with tags inside
                            var textContent = ""
                            node.childNodes().forEach { pNode ->
                                when (pNode) {
                                    is Element -> {
                                        when (pNode.tag().name) {
                                            "img" -> {
                                                lastDetail = DetailImage(pNode.attr("src"))
                                                listDetails.add(lastDetail)
                                            }
                                            else -> {
                                                textContent += pNode.text()
                                            }
                                        }
                                    }
                                    is TextNode -> {
                                        textContent += pNode.text()
                                    }
                                }
                            }
                            if (!textContent.isNullOrEmpty()) {
                                lastDetail = DetailText(textContent)
                                listDetails.add(lastDetail)
                            }
                        }
                        "img" -> {
                            lastDetail = DetailImage(node.attr("src"))
                            listDetails.add(lastDetail)
                        }
                        "figure" -> {
                            val img = node.selectFirst("img")
                            lastDetail = DetailImage(img.attr("src"))
                            listDetails.add(lastDetail)
                        }
                        "ul" -> {
                            val list = ArrayList<String>()
                            node.select("li").forEach { li ->
                                list.add(li.text())
                            }
                            lastDetail = DetailList(DetailList.ListType.UNORDERED, list)
                            listDetails.add(lastDetail)
                            //todo check text styles inside list item
                        }
                        "ol" -> {
                            val list = ArrayList<String>()
                            node.select("li").forEach { li ->
                                list.add(li.text())
                            }
                            lastDetail = DetailList(DetailList.ListType.ORDERED, list)
                            listDetails.add(lastDetail)
                        }
                        "br" -> {

                        }
                        "div" -> { //embeded
                            if (node.hasClass("oembed")) {
                                val iframe = node.select("iframe")

                                val html = "<iframe width=\"100%\" height=\"auto\"\n" +
                                        "                                style=\"display: block; border-style:none; margin: 0 auto; max-width: 100% !important;\"\n" +
                                        "                                src=\"${iframe.attr("src")}\"></iframe>"

                                lastDetail = DetailEmbedded(html)
                                listDetails.add(lastDetail)
                            } else if (node.childNode(0).attr("class").contains("table", true)) {
                                lastDetail = DetailUnknown("[TABLE]\n" + node.text())
                                listDetails.add(lastDetail)
                            } else {
                                lastDetail = DetailUnknown("[${node.attr("class")}] \n" + node.text())
                                listDetails.add(lastDetail)
                            }
                        }
                        "a" -> {
                            if (node.text().length > 0) {
                                //todo app here
                            } else {

                            }
                        }
                        "blockquote" -> {
                            lastDetail = DetailQuote(node.text())
                            listDetails.add(lastDetail)
                        }
                        "pre" -> {
                            lastDetail = DetailCode(node.text())
                            listDetails.add(lastDetail)
                        }
                        else -> {
                            lastDetail = DetailUnknown(node.text())
                            listDetails.add(lastDetail)
                        }
                    }
                }
                is TextNode -> {
                    if (node.text().trim().isNotEmpty()) {
                        lastDetail = DetailText(node.text())
                        listDetails.add(lastDetail)
                    }
                }
            }
        }

        return listDetails
    }
}