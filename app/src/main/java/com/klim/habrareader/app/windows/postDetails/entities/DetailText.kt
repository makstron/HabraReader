package com.klim.habrareader.app.windows.postDetails.entities

class DetailText(var content: String) : DetailBase() {

    init {
        type = PostDetailType.TEXT
    }

}