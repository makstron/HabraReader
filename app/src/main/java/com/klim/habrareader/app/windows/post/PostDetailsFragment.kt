package com.klim.habrareader.app.windows.post

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
import com.klim.habrareader.databinding.FragmentPostDetailsBinding


class PostDetailsFragment : BaseFragment(), LifecycleOwner {

    private lateinit var binding: FragmentPostDetailsBinding
    private lateinit var vm: PostDetailsFragmentVM

    @SuppressLint("ValidFragment")
    companion object {

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}