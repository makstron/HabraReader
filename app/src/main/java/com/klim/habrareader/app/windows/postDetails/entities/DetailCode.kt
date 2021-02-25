package com.klim.habrareader.app.windows.postDetails.entities

class DetailCode(val content: String) : DetailBase() {

    init {
        type = PostDetailType.CODE
    }

}