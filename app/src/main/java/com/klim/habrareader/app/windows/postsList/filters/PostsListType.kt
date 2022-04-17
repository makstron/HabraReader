package com.klim.habrareader.app.windows.postsList.filters

import com.klim.habrareader.R
import com.klim.habrareader.app.views.spinner.SpinnerItemI
import com.klim.habrareader.app.views.spinner.SpinnerItemTypes

enum class PostsListType(
    val _id: Long,
    val label: Int
) : SpinnerItemI {

    ALL_POSTS(1, R.string.post_list_type_all),
    BEST_POSTS(2, R.string.post_list_type_best);

    override fun getType() = SpinnerItemTypes.ITEM

    override fun getName() = null

    override fun getNameResId(): Int = label

    override fun getId() = _id

}