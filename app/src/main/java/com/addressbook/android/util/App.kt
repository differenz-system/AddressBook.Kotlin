package com.addressbook.android.util

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

/**
 * Created by Administrator on 3/14/18.
 */
class App : MultiDexApplication() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        instance = this

    }
}