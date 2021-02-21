package com.klim.habrareader.app.windows.postsList.filters

import com.klim.habrareader.App
import com.klim.habrareader.R
import com.klim.habrareader.app.views.BaseSpinnerAdapter

enum class PostListType(
    var _id: Long = 0L,
    var label: Int = 0
) : BaseSpinnerAdapter.Item {
    ALL_POSTS(1, R.string.post_list_type_all),
    BEST_POSTS(2, R.string.post_list_type_best);

    override fun getType() = BaseSpinnerAdapter.ItemTypes.ITEM

    override fun getName() = App.getApp().getString(label)

    override fun getId() = _id

}