package com.klim.habrareader.app.windows

import androidx.fragment.app.Fragment
import com.klim.habrareader.app.managers.windows_manager.WindowsContainerActivity

abstract class BaseFragment() : Fragment() {

    fun startWindow(fragment: BaseFragment, isItBase: Boolean = false) {
        if (activity is WindowsContainerActivity) {
            (activity as WindowsContainerActivity).startWindow(fragment, isItBase)
        } else {
            throw Exception("I can`t do these. Base Activity should implement WindowsContainerActivity.")
        }
    }

}