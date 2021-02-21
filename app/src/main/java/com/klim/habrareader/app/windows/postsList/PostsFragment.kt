package com.klim.habrareader.app.windows.postsList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.klim.habrareader.R
import com.klim.habrareader.app.BaseFragment
import com.klim.habrareader.app.views.BaseSpinnerAdapter
import com.klim.habrareader.app.windows.post.PostDetailsFragment
import com.klim.habrareader.app.windows.postsList.entities.PostView
import com.klim.habrareader.app.windows.postsList.enums.Commands
import com.klim.habrareader.app.windows.postsList.filters.PostListType
import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.databinding.FragmentPostsBinding


class PostsFragment : BaseFragment(), LifecycleOwner {

    private lateinit var binding: FragmentPostsBinding
    private lateinit var vm: PostsFragmentVM

    private lateinit var adapter: PostsAdapter
    private lateinit var adapterList: BaseSpinnerAdapter<PostListType>
    private lateinit var adapterThreshold: BaseSpinnerAdapter<PostsThreshold>
    private lateinit var adapterPeriod: BaseSpinnerAdapter<PostsPeriod>

    @SuppressLint("ValidFragment")
    companion object {

        fun newInstance(args: Bundle?): PostsFragment {
            val myFragment = PostsFragment()
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
        vm = ViewModelProvider(this).get(PostsFragmentVM::class.java)

        vm.lastChangedItem.observe(this, { updateCommand ->
            adapter.clear()
            adapter.addAll(vm.data)
            when (updateCommand.command) {
                Commands.DO_NOTHING -> {
                }
                Commands.UPDATE_All -> {
                    adapter.notifyDataSetChanged()
                }
                Commands.ITEM_CHANGED -> {
                    adapter.notifyItemChanged(updateCommand.position)
                }
                Commands.ITEM_INSERTED -> {
                    adapter.notifyItemInserted(updateCommand.position)
                }
                Commands.ITEM_REMOVED -> {
                    adapter.notifyItemRemoved(updateCommand.position)
                }
            }
        })

        vm.isLoading.observe(this, { data ->
            binding.srlRefresh.isRefreshing = data
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_posts, container, false)

        val layoutManager = LinearLayoutManager(context)

        adapter = PostsAdapter(requireContext(), ArrayList()) { view ->
            val postView = view.tag as PostView
            val args = Bundle()
            args.putInt(PostDetailsFragment.POST_ID, postView.id)
            startWindow(PostDetailsFragment.newInstance(args), false)
        }
        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
//                if (positionStart == 0 && positionStart == layoutManager.findFirstCompletelyVisibleItemPosition()) {
                    layoutManager.scrollToPosition(0)
//                }
            }
        })

        adapterList = BaseSpinnerAdapter(context)
        adapterList.items = listOf(PostListType.ALL_POSTS, PostListType.BEST_POSTS)
        adapterList.setDropDownViewResource(R.layout.simple_spinner_item, android.R.id.text1)

        adapterThreshold = BaseSpinnerAdapter(context)
        adapterThreshold.items = listOf(PostsThreshold.MORE_THAN_0, PostsThreshold.MORE_THAN_10, PostsThreshold.MORE_THAN_25, PostsThreshold.MORE_THAN_50, PostsThreshold.MORE_THAN_100)
        adapterThreshold.setDropDownViewResource(R.layout.simple_spinner_item, android.R.id.text1)

        adapterPeriod = BaseSpinnerAdapter(context)
        adapterPeriod.items = listOf(PostsPeriod.PERIOD_DAY, PostsPeriod.PERIOD_WEEK, PostsPeriod.PERIOD_MONTH, PostsPeriod.PERIOD_YEAR)
        adapterPeriod.setDropDownViewResource(R.layout.simple_spinner_item, android.R.id.text1)

        binding.apply {
            rvDataList.layoutManager = layoutManager
            rvDataList.adapter = adapter

            binding.sListType.adapter = adapterList
            adapterList.notifyDataSetChanged()

            binding.sListThreshold.adapter = adapterThreshold
            adapterThreshold.notifyDataSetChanged()

            binding.sListPeriod.adapter = adapterPeriod
            adapterPeriod.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActions()
    }

    private fun setActions() {
        binding.apply {
            srlRefresh.setOnRefreshListener {
                vm.updatePostList(true, sListType.selectedItem as PostListType, sListThreshold.selectedItem as PostsThreshold, sListPeriod.selectedItem as PostsPeriod, true)
            }

            srlRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )

            sListType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when (id) {
                        PostListType.ALL_POSTS._id -> {
                            binding.sListThreshold.visibility = View.VISIBLE
                            binding.sListPeriod.visibility = View.GONE
                        }
                        PostListType.BEST_POSTS._id -> {
                            binding.sListThreshold.visibility = View.GONE
                            binding.sListPeriod.visibility = View.VISIBLE
                        }
                    }
                    vm.updatePostList(true, sListType.selectedItem as PostListType, sListThreshold.selectedItem as PostsThreshold, sListPeriod.selectedItem as PostsPeriod)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

            sListThreshold.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    vm.updatePostList(true, sListType.selectedItem as PostListType, sListThreshold.selectedItem as PostsThreshold, sListPeriod.selectedItem as PostsPeriod)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            sListPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    vm.updatePostList(true, sListType.selectedItem as PostListType, sListThreshold.selectedItem as PostsThreshold, sListPeriod.selectedItem as PostsPeriod)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }
}