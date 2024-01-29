package com.livmas.tictactab.data.repositories

import android.util.Log
import com.livmas.tictactab.SETTINGS_TAG
import com.livmas.tictactab.data.sources.SettingsSharedPreferencesDataSource
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class SettingsRepository {
    companion object{
        val instance: SettingsRepository by lazy {
            SettingsRepository()
        }
    }

    private val source = SettingsSharedPreferencesDataSource.instance

    fun putCompGameMode(mode: ComplexGameMode) = source.putCompGameMode(mode.value)
    fun putNightMode(isNight: Boolean) = source.putNightMode(isNight)
    fun putUseNightMode(b: Boolean) = source.putUseNightMode(b)
    fun readCompGameMode() = ComplexGameMode.values()[source.readCompGameMode()].also {
        Log.d(SETTINGS_TAG, "Game mode gained from settings sp: $it") }
    fun readNightMode(): Boolean = source.readNightMode().also {
        Log.d(SETTINGS_TAG, "Night mode gained from settings sp: $it") }
    fun readUseNightMode(): Boolean = source.readUseNightMode().also {
        Log.d(SETTINGS_TAG, "Use night mode gained from settings sp: $it") }
}