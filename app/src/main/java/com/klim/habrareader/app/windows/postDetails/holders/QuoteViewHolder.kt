package com.klim.habrareader.app.windows.postDetails.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.klim.habrareader.R

class QuoteViewHolder (var view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView

    init {
        text = view.findViewById<TextView>(R.id.tvText)
    }
}