package com.klim.habrareader.app.windows.postsList.filters

import com.klim.habrareader.R
import com.klim.habrareader.app.views.spinner.SpinnerItemI
import com.klim.habrareader.app.views.spinner.SpinnerItemTypes

enum class PostsPeriod(
    val _id: Long,
    val label: Int
) : SpinnerItemI {

    PERIOD_DAY(1, R.string.post_period_type_day),
    PERIOD_WEEK(2, R.string.post_period_type_week),
    PERIOD_MONTH(3, R.string.post_period_type_month),
    PERIOD_YEAR(4, R.string.post_period_type_year),
    PERIOD_ALL_TIME(5, R.string.post_period_type_all_time);

    override fun getType() = SpinnerItemTypes.ITEM

    override fun getName() = null

    override fun getNameResId(): Int = label

    override fun getId() = _id

}
