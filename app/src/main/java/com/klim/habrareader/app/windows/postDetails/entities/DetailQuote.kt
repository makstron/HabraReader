package com.klim.habrareader.app.windows.postDetails.entities

class DetailQuote(val content: String) : DetailBase() {

    init {
        type = PostDetailType.QUOTE
    }

}