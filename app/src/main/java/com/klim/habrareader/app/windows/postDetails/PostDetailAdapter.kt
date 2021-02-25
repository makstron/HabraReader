package com.klim.habrareader.app.windows.postDetails

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.klim.habrareader.R
import com.klim.habrareader.app.windows.postDetails.entities.*
import com.klim.habrareader.app.windows.postDetails.holders.*
import com.klim.habrareader.databinding.*
import com.squareup.picasso.Picasso

class PostDetailAdapter(val context: Context, val data: ArrayList<DetailBase>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var textColor: Int = Color.RED

    init {
        textColor = ContextCompat.getColor(context, R.color.post_thumb_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            PostDetailType.TITLE.id -> {
                var a = DataBindingUtil.inflate<ItemPostDetailsTitleBinding>(LayoutInflater.from(parent.context), R.layout.item_post_details_title, parent, false)
                return TitleViewHolder(a.root)
            }
            PostDetailType.TEXT.id -> {
                var a = DataBindingUtil.inflate<ItemPostDetailsTextBinding>(LayoutInflater.from(parent.context), R.layout.item_post_details_text, parent, false)
                return TextViewHolder(a.root)
            }
            PostDetailType.IMAGE.id -> {
                var a = DataBindingUtil.inflate<ItemPostDetailsImageBinding>(LayoutInflater.from(parent.context), R.layout.item_post_details_image, parent, false)
                return ImageViewHolder(a.root)
            }
            PostDetailType.LIST.id -> {
                var a = DataBindingUtil.inflate<ItemPostDetailsListBinding>(LayoutInflater.from(parent.context), R.layout.item_post_details_list, parent, false)
                return ListViewHolder(a.root)
            }
            PostDetailType.EMBEDDED.id -> {
                var a = DataBindingUtil.inflate<ItemPostDetailsEmbededBinding>(LayoutInflater.from(parent.context), R.layout.item_post_details_embeded, parent, false)
                return EmbeddedViewHolder(a.root)
            }
            PostDetailType.QUOTE.id -> {
                var a = DataBindingUtil.inflate<ItemPostDetailsQuoteBinding>(LayoutInflater.from(parent.context), R.layout.item_post_details_quote, parent, false)
                return QuoteViewHolder(a.root)
            }
            PostDetailType.CODE.id -> {
                var a = DataBindingUtil.inflate<ItemPostDetailsCodeBinding>(LayoutInflater.from(parent.context), R.layout.item_post_details_code, parent, false)
                return CodeViewHolder(a.root)
            }
            PostDetailType.EMPTY.id -> {
                var a = DataBindingUtil.inflate<ItemPostDetailsEmptyBinding>(LayoutInflater.from(parent.context), R.layout.item_post_details_empty, parent, false)
                return EmptyViewHolder(a.root)
            }
            PostDetailType.DIVIDER.id -> {
                var a = DataBindingUtil.inflate<ItemPostDetailsDividerBinding>(LayoutInflater.from(parent.context), R.layout.item_post_details_divider, parent, false)
                return DividerViewHolder(a.root)
            }
            PostDetailType.UNKNOWN.id -> {
                var a = DataBindingUtil.inflate<ItemPostDetailsUncnownBinding>(LayoutInflater.from(parent.context), R.layout.item_post_details_uncnown, parent, false)
                return UncnownViewHolder(a.root)
            }
            else -> {
                val a = DataBindingUtil.inflate<ItemPostDetailsUncnownBinding>(LayoutInflater.from(parent.context), R.layout.item_post_details_uncnown, parent, false)
                return UncnownViewHolder(a.root)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].type.id
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            PostDetailType.TITLE.id -> {
                val viewHolder = (holder as TitleViewHolder)
                val item = data[position] as DetailTitle
                viewHolder.title.text = item.content
                viewHolder.title.textSize = item.size.size
            }
            PostDetailType.TEXT.id -> {
                val viewHolder = (holder as TextViewHolder)
                val item = data[position] as DetailText
                viewHolder.text.text = item.content
            }
            PostDetailType.IMAGE.id -> {
                val viewHolder = (holder as ImageViewHolder)
                val item = data[position] as DetailImage

                Picasso.get()
                    .load(item.url)
                    .placeholder(R.drawable.ic_terrain_wide)
                    .error(R.drawable.ic_terrain_wide)
                    .into(viewHolder.imageView)
            }
            PostDetailType.LIST.id -> {
                val viewHolder = (holder as ListViewHolder)
                val item = data[position] as DetailList

                var order = 1
                viewHolder.lloList.removeAllViews()
                item.items.forEach { li ->
                    var tv = TextView(viewHolder.lloList.context)
                    tv.setTextColor(textColor)
                    when (item.listType) {
                        DetailList.ListType.ORDERED -> {
                            tv.text = "$order. $li"
                            order++
                        }
                        DetailList.ListType.UNORDERED -> {
                            tv.text = "• $li"
                        }
                    }
                    viewHolder.lloList.addView(tv)
                }
            }
            PostDetailType.EMBEDDED.id -> {
                val viewHolder = (holder as EmbeddedViewHolder)
                val item = data[position] as DetailEmbedded
                viewHolder.wvEmbedded.settings.javaScriptEnabled = true
                viewHolder.wvEmbedded.loadData(item.content, "text/html; charset=UTF-8", null)
            }
            PostDetailType.QUOTE.id -> {
                val viewHolder = (holder as QuoteViewHolder)
                val item = data[position] as DetailQuote
                viewHolder.text.text = item.content
            }
            PostDetailType.CODE.id -> {
                val viewHolder = (holder as CodeViewHolder)
                val item = data[position] as DetailCode
                viewHolder.text.text = item.content
            }
            PostDetailType.EMPTY.id -> {
                val viewHolder = (holder as EmptyViewHolder)
                val item = data[position] as DetailEmpty
            }
            PostDetailType.DIVIDER.id -> {
                val viewHolder = (holder as DividerViewHolder)
                val item = data[position] as DetailDivider
            }
            PostDetailType.UNKNOWN.id -> {
                val viewHolder = (holder as UncnownViewHolder)
                val item = data[position] as DetailUnknown
                viewHolder.text.text = item.content
            }
        }
    }


    override fun getItemCount(): Int = data.size

    fun clear() {
        data.clear()
    }

    fun addAll(list: List<DetailBase>) {
        data.addAll(list)
    }
}