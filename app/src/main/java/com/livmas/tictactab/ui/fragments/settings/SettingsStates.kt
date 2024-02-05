package com.livmas.tictactab.ui.fragments.settings

import com.livmas.tictactab.data.repositories.SettingsRepository
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class SettingsStates {
    private val repository = SettingsRepository.instance


    var complexGameMode: ComplexGameMode = repository.readCompGameMode()
    var nightTheme: Boolean = repository.readNightMode()
    var useNightTheme: Boolean = repository.readUseNightMode()

    private fun saveCompGameMode(mode: ComplexGameMode?) = repository.putCompGameMode(mode ?: ComplexGameMode.Stack)
    private fun saveNightMode(isNight: Boolean?) = repository.putNightMode(isNight?: false)
    private fun saveUseNightMode(b: Boolean?) = repository.putUseNightMode(b?: false)

    fun saveData() {
        saveCompGameMode(complexGameMode)
        saveNightMode(nightTheme)
        saveUseNightMode(useNightTheme)
    }
    fun loadData() {
        complexGameMode = repository.readCompGameMode()
        nightTheme = repository.readNightMode()
        useNightTheme = repository.readUseNightMode()
    }
}