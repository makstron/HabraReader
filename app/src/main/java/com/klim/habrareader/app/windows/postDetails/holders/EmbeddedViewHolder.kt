package com.klim.habrareader.app.windows.postDetails.holders

import android.view.View
import android.webkit.WebView
import androidx.recyclerview.widget.RecyclerView
import com.klim.habrareader.R

class EmbeddedViewHolder (var view: View) : RecyclerView.ViewHolder(view) {
    val wvEmbedded: WebView

    init {
        wvEmbedded = view.findViewById<WebView>(R.id.wvEmbedded)
    }
}