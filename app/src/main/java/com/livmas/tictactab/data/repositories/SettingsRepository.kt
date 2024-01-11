package com.livmas.tictactab.data.repositories

import android.util.Log
import com.livmas.tictactab.SETTINGS_TAG
import com.livmas.tictactab.data.sources.SettingsSharedPreferencesDataSource
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class SettingsRepository {
    private val source = SettingsSharedPreferencesDataSource.instance

    fun putCompGameMode(mode: ComplexGameMode) = source.putCompGameMode(mode.value)
    fun putTheme(isNight: Boolean) = source.putTheme(isNight)
    fun readCompGameMode() = ComplexGameMode.values()[source.readCompGameMode()].also {
        Log.d(SETTINGS_TAG, it.toString()) }
}