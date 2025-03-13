package com.diegorezm.ratemymusic

import android.app.Application
import com.diegorezm.ratemymusic.di.AppModule
import com.diegorezm.ratemymusic.di.AppModuleImpl

class RateMyMusicApp : Application() {

    companion object {
        lateinit var appModule: AppModule
    }

    override

    fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this@RateMyMusicApp)
    }
}