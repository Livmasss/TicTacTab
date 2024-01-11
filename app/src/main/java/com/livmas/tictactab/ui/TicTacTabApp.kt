package com.livmas.tictactab.ui

import android.app.Application
import com.livmas.tictactab.data.sources.SettingsSharedPreferencesDataSource

class TicTacTabApp: Application() {

    override fun onCreate() {
        super.onCreate()

        SettingsSharedPreferencesDataSource.instance.initiate(applicationContext)
    }
}