package com.klim.habrareader.app.windows.postDetails.holders

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.klim.habrareader.R

class ImageViewHolder (var view: View) : RecyclerView.ViewHolder(view) {
    val imageView: ImageView

    init {
        imageView = view.findViewById<ImageView>(R.id.ivImage)
    }
}