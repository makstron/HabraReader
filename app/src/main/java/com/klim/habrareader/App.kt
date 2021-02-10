package com.klim.habrareader

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex


class App : Application() {

    companion object {
        private lateinit var app: App

        fun getApp() : App = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}