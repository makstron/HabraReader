package com.klim.habrareader.app.activities.main

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.klim.habrareader.R
import com.klim.habrareader.app.BaseFragment
import com.klim.habrareader.app.managers.windows_manager.WindowsContainerActivity
import com.klim.habrareader.app.managers.windows_manager.WindowsManager
import com.klim.habrareader.app.managers.windows_manager.views.WindowsContainer
import com.klim.habrareader.app.utils.AndroidMeasurementsU
import com.klim.habrareader.app.utils.ConvertU
import com.klim.habrareader.app.windows.postsList.PostsFragment
import com.klim.habrareader.databinding.ActivityMainBinding
import com.klim.habrareader.domain.entities.PostThumbEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import kotlin.random.Random


class MainActivity : AppCompatActivity(), WindowsContainerActivity {

    lateinit var binding: ActivityMainBinding
    lateinit var vm: MainActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_HabraReader_NoActionBar)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        vm = ViewModelProvider(this).get(MainActivityVM::class.java)


        binding.apply {
            wcWindowsContainer.windowsManager = WindowsManager(this@MainActivity)
        }

        startWindow(PostsFragment.newInstance(Bundle()), true)

//        GlobalScope.launch(Dispatchers.Default) {
//            var succes = 0
//            for (game in 0..100_000_000) {
////                print("Game $game   ")
//                var list = arrayListOf<Int>(0,1,2,3,4,5,6,7,8,9)
//                var secret = list.random()
//
//                var guess = list.random()
//                if (secret == guess) {
////                    println("succes")
//                    succes++
//                    continue
//                } else {
//                    list.remove(guess)
//                }
//
//                var guess2 = list.random()
//                if (secret == guess2) {
////                    println("succes")
//                    succes++
//                } else {
////                    println("fail")
//                }
//
//                if (game % 1_000_000 == 0) {
//                    println("Game $game")
//                }
//            }
//
//            println("Game succeed $succes")
//            println("Game succeed percent ${succes/100_000_000}")
//        }

//        GlobalScope.launch {
//            val channel = Channel<Int>()
//            println("________________")
//            launch {
//                for (it in 0..10) {
//                    println("Emit $it in thread ${Thread.currentThread()}")
//                    channel.send(it)
//                    delay(1000)
//                }
//            }
//
//            delay(500)
//
//            launch(Dispatchers.Default) {
////                channel.consumeEach {
////                    println("First $it")
////                }
//                for (post in channel) {
//                    println("First $post")
//                }
//            }
//
//            launch(Dispatchers.Default) {
////                channel.consumeEach {
////                    println("First $it")
////                }
//                for (post in channel) {
//                    println("Second $post")
//                }
//            }
//
//            println("________________")
//        }

//        GlobalScope.launch(Dispatchers.Main) {
//
//            val _tickFlow = MutableSharedFlow<Int>(replay = 0)
//            val tickFlow: SharedFlow<Int> = _tickFlow
//
//
//
//            launch(Dispatchers.IO) {
//                tickFlow.collect {
//                    println("@!__ First $it collect in ${Thread.currentThread()}")
//                }
//            }
//
////            delay(1000)
//            launch(Dispatchers.IO) {
//                tickFlow
//                    .collect {
//                        delay(1000)
//                    println("@!__ Second $it collect in ${Thread.currentThread()}")
//                }
//            }
//
//            launch(Dispatchers.IO) {
//                listOf<Int>(1, 2, 3).forEach {
//                    delay(0)
//                    println("@!__ Emit $it in thread ${Thread.currentThread()}")
//                    _tickFlow.emit(it)
//                }
//            }
//
//        }

//        GlobalScope.launch(Dispatchers.IO) {
//            withContext(Dispatchers.IO) {
//                val flo = flow {
//                    listOf<Int>(1, 2, 3).forEach {
////                        delay(1000)
//                        println("Emit $it in thread ${Thread.currentThread()}")
//                        if (it == 2) it / 0
//                        emit(it)
//                    }
//                }
//                launch(Dispatchers.IO) {
////                    flo.can
//                    flo.flowOn(Dispatchers.IO)
//                        .catch { e->
//                            e.printStackTrace()
//                        }
//                        .collect {
//                            println("First $it collect in ${Thread.currentThread()}")
//                        }
////                flo.collect {
////                    println("Second $it")
////                }
//                }
//            }
//
//
////            launch(Dispatchers.IO) {
////                flo.collect {
////                    println("Second $it")
////                }
////            }
//        }


//        GlobalScope.launch {
//            println("@__ launch started")
//
//            var a = async(Dispatchers.Default) {
//                println("@__ async a started")
//                delay(1000)
//                val a = 10 / 0
//                println("@__ async a finished")
//            }
//
//            var b = async(Dispatchers.Default) {
//                println("@__ async b started")
//                delay(1000)
//                throw Exception("op op op")
//                println("@__ async b finished")
//            }
//
//            delay(2000)
//
//            println("@__ launch before await a")
//            a.await()
//            println("@__ launch after await a")
//            println("@__ launch before await b")
//            try {
//                b.await()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            println("@__ launch after await b")
//        }
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