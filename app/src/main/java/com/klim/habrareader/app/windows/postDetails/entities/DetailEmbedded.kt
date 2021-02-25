package com.klim.habrareader.app.windows.postDetails.entities

class DetailEmbedded(val content: String) : DetailBase() {

    init {
        type = PostDetailType.EMBEDDED
    }

}