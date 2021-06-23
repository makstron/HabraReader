package com.klim.habrareader

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.klim.habrareader.app.di.viewModelModule
import com.klim.habrareader.data.db.DbHelper
import com.squareup.picasso.Picasso
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    companion object {
        private lateinit var app: App

        fun getApp() : App = app
    }

    override fun onCreate() {
        super.onCreate()
        app = this

        DbHelper.init(this)

        System.setProperty("kotlinx.coroutines.debug", if (BuildConfig.DEBUG) "on" else "off")

        Picasso.get().isLoggingEnabled = false
        Picasso.get().setIndicatorsEnabled(false)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(viewModelModule))
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}