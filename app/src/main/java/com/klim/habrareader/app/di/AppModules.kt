package com.klim.habrareader.app.di

import com.klim.habrareader.app.windows.postsList.PostsFragmentVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PostsFragmentVM(get())
    }
}
