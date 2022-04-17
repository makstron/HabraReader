package com.klim.habrareader.app.windows.postsList.filters

import com.klim.habrareader.R
import com.klim.habrareader.app.views.spinner.SpinnerItemI
import com.klim.habrareader.app.views.spinner.SpinnerItemTypes

enum class PostsThreshold(
    val _id: Long,
    val label: Int
) : SpinnerItemI {

    MORE_THAN_ALL(1, R.string.post_rating_all),
    MORE_THAN_0(2, R.string.post_rating_0),
    MORE_THAN_10(3, R.string.post_rating_10),
    MORE_THAN_25(4, R.string.post_rating_25),
    MORE_THAN_50(5, R.string.post_rating_50),
    MORE_THAN_100(6, R.string.post_rating_100);

    override fun getType() = SpinnerItemTypes.ITEM

    override fun getName() = null

    override fun getNameResId(): Int = label

    override fun getId() = _id

}