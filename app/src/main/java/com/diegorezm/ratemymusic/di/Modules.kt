package com.diegorezm.ratemymusic.di

import com.diegorezm.ratemymusic.core.data.HttpClientFactory
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(get()) }
}