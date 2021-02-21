package com.klim.habrareader.app.windows.postsList

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.klim.habrareader.R
import com.klim.habrareader.app.utils.ResourceUtil
import com.klim.habrareader.app.windows.postsList.entities.PostView
import com.squareup.picasso.Picasso

class PostsAdapter(
    var context: Context,
    var data: ArrayList<PostView>,
    val clickListener: View.OnClickListener
) : RecyclerView.Adapter<PostViewHolder>() {

    private val placeholder: Drawable

    init {
        placeholder = ResourceUtil.getColoredRes(context, R.drawable.ic_terrain, ContextCompat.getColor(context, R.color.post_thumb_image_placeholder))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_thumb_post, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = data[position]
        holder.binding.apply {
            tvTitle.text = item.title
            tvDescription.text = item.shortDescription
            tvVotesCount.text = if (item.votesCount == 0) " ${item.votesCount}" else if (item.votesCount > 0) "+${item.votesCount}" else item.votesCount.toString()
            tvVotesCount.setTextColor(
                ContextCompat.getColor(
                    tvVotesCount.context,
                    if (item.votesCount == 0) R.color.votes_neutral else if (item.votesCount < 0) R.color.votes_red else R.color.votes_green
                )
            )
            tvBookmarksCount.text = item.bookmarksCount
            tvViewCount.text = item.viewsCount
            tvCommentsCount.text = item.commentsCount
            tvCreatedTime.text = item.createdTime
            tvAuthor.text = item.author

            if (!item.postImage.isNullOrBlank()) {
                ivPostImage.visibility = View.VISIBLE

                Picasso.get()
                    .load(item.postImage)
                    .placeholder(placeholder)
                    .error(placeholder)
                    .into(ivPostImage)

            } else {
                ivPostImage.visibility = View.GONE
            }

            ivAuthorLogo.setImageResource(R.drawable.ic_user_icon_placeholder)
            if (!item.authorIcon.isNullOrEmpty()) {
                Picasso.get()
                    .load(item.authorIcon)
                    .placeholder(R.drawable.ic_user_icon_placeholder)
                    .error(R.drawable.ic_user_icon_placeholder)
                    .into(ivAuthorLogo)
            }

            cvContainer.tag = item
            cvContainer.setOnClickListener(clickListener)
        }
    }

    override fun getItemCount(): Int = data.size

    fun clear() {
        data.clear()
    }

    fun addAll(list: List<PostView>) {
        data.addAll(list)
    }
}