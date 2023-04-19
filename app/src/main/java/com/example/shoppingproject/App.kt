package com.example.shoppingproject

import android.app.Application
import com.example.shoppingproject.remote.di.firebaseModule
import com.example.shoppingproject.remote.di.localModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                firebaseModule,
                localModule
            )
        }
    }
}