package com.klim.habrareader.app.windows.postDetails.entities

import com.klim.habrareader.app.windows.postDetails.enums.TitleSizesEnum

class DetailTitle(val content: String, val size: TitleSizesEnum) : DetailBase() {

    init {
        type = PostDetailType.TITLE
    }

}