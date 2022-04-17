package com.klim.habrareader.app.activities.main

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.klim.habrareader.R
import com.klim.habrareader.app.windows.BaseFragment
import com.klim.habrareader.app.managers.windows_manager.WindowsContainerActivity
import com.klim.habrareader.app.managers.windows_manager.WindowsKeeper
import com.klim.habrareader.app.managers.windows_manager.views.WindowsContainer
import com.klim.habrareader.app.windows.postsList.PostsFragment
import com.klim.habrareader.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class MainActivity : AppCompatActivity(), WindowsContainerActivity {

    private lateinit var binding: ActivityMainBinding
    private lateinit var vm: MainActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_HabraReader_NoActionBar)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        vm = ViewModelProvider(this).get(MainActivityVM::class.java)
        customizeView()
        binding.wcWindowsContainer.windowsKeeper = WindowsKeeper(this@MainActivity)

        startWindow(PostsFragment.newInstance(Bundle()), true)
    }

    private fun customizeView() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION and SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_bg)
        window.navigationBarColor = Color.GRAY
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