package com.klim.habrareader.app.activities.main

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.klim.habrareader.R
import com.klim.habrareader.app.BaseFragment
import com.klim.habrareader.app.managers.windows_manager.WindowsContainerActivity
import com.klim.habrareader.app.managers.windows_manager.WindowsManager
import com.klim.habrareader.app.managers.windows_manager.views.WindowsContainer
import com.klim.habrareader.app.windows.postsList.PostsFragment
import com.klim.habrareader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WindowsContainerActivity {

    lateinit var binding: ActivityMainBinding
    lateinit var vm: MainActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_HabraReader_NoActionBar)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        vm = ViewModelProvider(this).get(MainActivityVM::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        binding.apply {
            wcWindowsContainer.windowsManager = WindowsManager(this@MainActivity)
        }

        startWindow(PostsFragment.newInstance(Bundle()), true)
    }

    override fun startWindow(fragment: BaseFragment, isItBase: Boolean) {
        binding.wcWindowsContainer.startWindow(fragment, isItBase)
    }

    override fun getActivityViewContainer(): WindowsContainer = binding.wcWindowsContainer

    override fun getContext(): Context {
        return this
    }

    override fun onBackPressed() {
        if (binding.wcWindowsContainer.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }
}