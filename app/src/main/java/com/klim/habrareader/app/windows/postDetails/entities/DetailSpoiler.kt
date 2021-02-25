package com.klim.habrareader.app.windows.postDetails.entities

class DetailSpoiler(val openText: String, val content: ArrayList<DetailBase>) : DetailBase() {

    init {
        type = PostDetailType.SPOILER
    }

}