package com.klim.habrareader.app.windows.postDetails.entities

class DetailUnknown(val content: String) : DetailBase() {

    init {
        type = PostDetailType.UNKNOWN
    }

}