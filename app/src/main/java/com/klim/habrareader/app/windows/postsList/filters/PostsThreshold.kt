package com.klim.habrareader.app.windows.postsList.filters

import com.klim.habrareader.app.views.BaseSpinnerAdapter

enum class PostsThreshold(
    var _id: Long = 0L,
    var label: String = ""
) : BaseSpinnerAdapter.Item {

    MORE_THAN_0(1, "≥0"),
    MORE_THAN_10(2, "≥10"),
    MORE_THAN_25(3, "≥25"),
    MORE_THAN_50(4, "≥50"),
    MORE_THAN_100(5, "≥100");

    override fun getType() = BaseSpinnerAdapter.ItemTypes.ITEM

    override fun getName() = label

    override fun getId() = _id

}