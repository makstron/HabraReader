package com.klim.habrareader.app.windows.postDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.klim.habrareader.R
import com.klim.habrareader.app.windows.BaseFragment
import com.klim.habrareader.app.utils.CharsPerLineUtil
import com.klim.habrareader.app.utils.ConvertU
import com.klim.habrareader.databinding.FragmentPostDetailsBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception


class PostDetailsFragment : BaseFragment(), LifecycleOwner {

    private lateinit var binding: FragmentPostDetailsBinding
    private lateinit var vm: PostDetailsFragmentVM

    private lateinit var adapter: PostDetailAdapter

    private var titleMaxRowLength = 0

    @SuppressLint("ValidFragment")
    companion object {
        const val POST_ID = "post_id"

        fun newInstance(args: Bundle?): PostDetailsFragment {
            val myFragment = PostDetailsFragment()

            args?.let {
                myFragment.arguments = args
            } ?: run {
                myFragment.arguments = Bundle()
            }

            return myFragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this).get(PostDetailsFragmentVM::class.java)

        arguments?.get(POST_ID)?.let { id ->
            vm.postId = id as Int
        }

        vm.isLoading.observe(this, { isLoading ->
            binding.srlRefresh.isRefreshing = isLoading
        })

        vm.data.observe(this, { data ->
            showData(data)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_details, container, false)

        binding.apply {
            rvContentList.layoutManager = LinearLayoutManager(context)
            adapter = PostDetailAdapter(requireContext(), ArrayList())
            rvContentList.adapter = adapter
            adapter.notifyDataSetChanged()

            srlRefresh.setOnRefreshListener {
                vm.updatePost(true)
            }

            srlRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

                binding.apply {

                    expandedImage.alpha = 1f - (-verticalOffset.toFloat() * 100f / appBarLayout.totalScrollRange.toFloat() / 100f)

                    val maxTranslationTitle = ConvertU.convertDpToPixels(33f)
                    tvTitle.translationY = (-verticalOffset.toFloat() * maxTranslationTitle / appBarLayout.totalScrollRange.toFloat())

                    val maxTranslation = ConvertU.convertDpToPixels(25f)

                    vm.data.value?.let {
                        val lengthTitle = it.title.length

                        if (titleMaxRowLength == 0) {
                            titleMaxRowLength = CharsPerLineUtil.getMaxCharsPerLine(tvTitle)
                            if (tvTitle.getLayout().getLineCount() > 1) {
                                titleMaxRowLength -= 3
                            }

                            var lp = CollapsingToolbarLayout.LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, tvTitle.height + ConvertU.convertDpToPixels(74f) - maxTranslation)
                            toolbar.layoutParams = lp
                        }
                    }

                    ivAuthorLogo.translationY = (-verticalOffset.toFloat() * maxTranslation / appBarLayout.totalScrollRange.toFloat())

                    val maxTranslationNick = ConvertU.convertDpToPixels(14f)
                    tvAuthor.translationY = (-verticalOffset.toFloat() * maxTranslationNick / appBarLayout.totalScrollRange.toFloat())

                    val maxTranslationScore = ConvertU.convertDpToPixels(14f)
                    val transliterator = (-verticalOffset.toFloat() * maxTranslationScore / appBarLayout.totalScrollRange.toFloat())

                    rvVotes.translationY = transliterator
                    rvBookmarks.translationY = transliterator
                    rvViews.translationY = transliterator
                    rvComments.translationY = transliterator
                }
            })

        vm.updatePost(true)
    }

    fun showData(data: PostDetailsView) {
        binding.apply {
            titleMaxRowLength = 0

            tvTitle.text = data.title
            tvCreatedTime.text = data.createdTimestamp
            rvVotes.setValue(data.votesCount)
            rvBookmarks.setValue(data.bookmarksCount)
            rvViews.setValue(data.viewsCount)
            rvComments.setValue(data.commentsCount)


            if (!data.postImageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(data.postImageUrl)
                    .placeholder(R.drawable.ic_terrain_wide)
                    .error(R.drawable.ic_terrain_wide)
                    .into(expandedImage)
            }

            if (!data.postImageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(data.postImageUrl)
                    .placeholder(R.drawable.ic_terrain_wide)
                    .error(R.drawable.ic_terrain_wide)
                    .into(expandedImage)
            }

            if (!data.authorAvatar.isNullOrEmpty()) {
                Picasso.get()
                    .load(data.authorAvatar)
                    .placeholder(R.drawable.ic_user_icon_placeholder)
                    .error(R.drawable.ic_user_icon_placeholder)
                    .into(ivAuthorLogo, object : Callback {
                        override fun onSuccess() {
                            ivAuthorLogo.background = null
                        }

                        override fun onError(e: Exception?) {
                            TODO("Not yet implemented")
                        }

                    })
            }

            tvAuthor.text = data.authorNickname

            adapter.clear()
            adapter.addAll(data.content)
            adapter.notifyDataSetChanged()
        }
    }
}