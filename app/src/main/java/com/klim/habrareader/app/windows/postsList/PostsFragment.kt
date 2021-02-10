package com.klim.habrareader.app.windows.postsList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.klim.habrareader.R
import com.klim.habrareader.app.BaseFragment
import com.klim.habrareader.app.windows.post.PostDetailsFragment
import com.klim.habrareader.databinding.FragmentPostsBinding


class PostsFragment : BaseFragment(), LifecycleOwner {

    private lateinit var binding: FragmentPostsBinding
    private lateinit var vm: PostsFragmentVM

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_posts, container, false)

        binding.apply {
            tvLabel.setOnClickListener {
                startWindow(PostDetailsFragment.newInstance(Bundle()))
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}