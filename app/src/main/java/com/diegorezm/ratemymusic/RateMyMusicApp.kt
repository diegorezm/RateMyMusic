package com.diegorezm.ratemymusic

import android.app.Application
import com.diegorezm.ratemymusic.di.initKoin
import org.koin.android.ext.koin.androidContext

class RateMyMusicApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@RateMyMusicApp)
        }
    }
}