package com.danilovalerio.mycoin

import android.app.Application
import com.danilovalerio.mycoin.di.ModulosDeDependencias
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
            this,
            listOf(ModulosDeDependencias.moduloApp,
                ModulosDeDependencias.moduloX
            )
        )
    }

}