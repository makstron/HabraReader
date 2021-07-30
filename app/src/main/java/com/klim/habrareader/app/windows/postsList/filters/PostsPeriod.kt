package com.klim.habrareader.app.windows.postsList.filters

import com.klim.habrareader.App
import com.klim.habrareader.R
import com.klim.habrareader.app.views.BaseSpinnerAdapter

enum class PostsPeriod(
    var _id: Long = 0L,
    var label: Int = 0
) : BaseSpinnerAdapter.Item {

    PERIOD_DAY(1, R.string.post_period_type_day),
    PERIOD_WEEK(2, R.string.post_period_type_week),
    PERIOD_MONTH(3, R.string.post_period_type_month),
    PERIOD_YEAR(4, R.string.post_period_type_year),
    PERIOD_ALL_TIME(5, R.string.post_period_type_all_time);

    override fun getType() = BaseSpinnerAdapter.ItemTypes.ITEM

    override fun getName() = App.getApp().getString(label)

    override fun getId() = _id

}
