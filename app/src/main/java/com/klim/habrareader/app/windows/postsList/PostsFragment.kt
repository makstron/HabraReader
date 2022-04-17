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
import com.klim.habrareader.app.windows.BaseFragment
import com.klim.habrareader.app.views.spinner.BaseSpinnerAdapter
import com.klim.habrareader.app.windows.postDetails.PostDetailsFragment
import com.klim.habrareader.app.windows.postsList.entities.PostView
import com.klim.habrareader.app.windows.postsList.enums.Commands
import com.klim.habrareader.app.windows.postsList.filters.PostsListType
import com.klim.habrareader.app.windows.postsList.filters.PostsPeriod
import com.klim.habrareader.app.windows.postsList.filters.PostsThreshold
import com.klim.habrareader.databinding.FragmentPostsBinding


class PostsFragment : BaseFragment(), LifecycleOwner {

    private lateinit var binding: FragmentPostsBinding
    private lateinit var vm: PostsFragmentVM

    private lateinit var adapter: PostsAdapter
    private lateinit var adapterList: BaseSpinnerAdapter<PostsListType>
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
        setVmObservers()
    }

    private fun setVmObservers() {
        vm.lastChangedItem.observe(this, { updateCommand ->
            adapter.clear()
            adapter.addAll(vm.postsViewList)
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
        adapterList.items = PostsListType.values().toList()
        adapterList.setDropDownViewResource(R.layout.simple_spinner_item, android.R.id.text1)

        adapterThreshold = BaseSpinnerAdapter(context)
        adapterThreshold.items = PostsThreshold.values().toList()
        adapterThreshold.setDropDownViewResource(R.layout.simple_spinner_item, android.R.id.text1)

        adapterPeriod = BaseSpinnerAdapter(context)
        adapterPeriod.items = PostsPeriod.values().toList()
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
                vm.updatePostList(true, sListType.selectedItem as PostsListType, sListThreshold.selectedItem as PostsThreshold, sListPeriod.selectedItem as PostsPeriod, true)
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
                        PostsListType.ALL_POSTS._id -> {
                            binding.sListThreshold.visibility = View.VISIBLE
                            binding.sListPeriod.visibility = View.GONE
                        }
                        PostsListType.BEST_POSTS._id -> {
                            binding.sListThreshold.visibility = View.GONE
                            binding.sListPeriod.visibility = View.VISIBLE
                        }
                    }
                    vm.updatePostList(true, sListType.selectedItem as PostsListType, sListThreshold.selectedItem as PostsThreshold, sListPeriod.selectedItem as PostsPeriod)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

            sListThreshold.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    vm.updatePostList(true, sListType.selectedItem as PostsListType, sListThreshold.selectedItem as PostsThreshold, sListPeriod.selectedItem as PostsPeriod)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

            sListPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    vm.updatePostList(true, sListType.selectedItem as PostsListType, sListThreshold.selectedItem as PostsThreshold, sListPeriod.selectedItem as PostsPeriod)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }
}