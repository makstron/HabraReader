package com.klim.habrareader.app.managers.windows_manager

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.klim.habrareader.app.windows.BaseFragment
import com.klim.habrareader.app.managers.windows_manager.views.WindowsContainer

interface WindowsContainerActivity {
    fun startWindow(fragment: BaseFragment, isItBase: Boolean = false)

    fun getActivityViewContainer(): WindowsContainer

    fun getSupportFragmentManager(): FragmentManager

    fun getContext(): Context

}