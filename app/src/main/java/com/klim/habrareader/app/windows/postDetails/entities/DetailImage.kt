package com.klim.habrareader.app.windows.postDetails.entities

class DetailImage(val url: String) : DetailBase() {

    init {
        type = PostDetailType.IMAGE
    }

}