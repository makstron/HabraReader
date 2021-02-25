package com.klim.habrareader.app.windows.postDetails.holders

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.klim.habrareader.R

class ListViewHolder (var view: View) : RecyclerView.ViewHolder(view) {
    val lloList: LinearLayout

    init {
        lloList = view.findViewById<LinearLayout>(R.id.lloList)
    }
}