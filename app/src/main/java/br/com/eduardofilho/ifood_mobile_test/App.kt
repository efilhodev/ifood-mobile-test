package br.com.eduardofilho.ifood_mobile_test

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import br.com.eduardofilho.ifood_mobile_test.network.ConnectivityReceiver

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        registerConnectivityReceiver()
    }

    init {
        instance = this

    }

    companion object {
        private var instance: App? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    private fun registerConnectivityReceiver(){
        registerReceiver(ConnectivityReceiver(),
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}