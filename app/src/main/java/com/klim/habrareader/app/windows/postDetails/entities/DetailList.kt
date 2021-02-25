package com.klim.habrareader.app.windows.postDetails.entities

class DetailList(val listType: ListType, val items: ArrayList<String>) : DetailBase() {

    enum class ListType {
        UNORDERED,
        ORDERED
    }

    init {
        type = PostDetailType.LIST
    }

}