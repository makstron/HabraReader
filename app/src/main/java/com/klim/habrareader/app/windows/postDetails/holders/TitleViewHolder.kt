package com.klim.habrareader.app.windows.postDetails.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.klim.habrareader.R

class TitleViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView

    init {
        title = view.findViewById<TextView>(R.id.tvTitle)
    }
}