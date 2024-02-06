package com.livmas.tictactab.ui.fragments.settings

import com.livmas.tictactab.data.repositories.SettingsRepository
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class SettingsState(
    var complexGameMode: ComplexGameMode,
    var nightTheme: Boolean,
    var useNightTheme: Boolean
) {
    constructor() : this(
        SettingsRepository.instance.readCompGameMode(),
        SettingsRepository.instance.readNightMode(),
        SettingsRepository.instance.readUseNightMode()
    )

    private fun saveCompGameMode(mode: ComplexGameMode?) = SettingsRepository.instance.putCompGameMode(mode ?: ComplexGameMode.Stack)
    private fun saveNightMode(isNight: Boolean?) = SettingsRepository.instance.putNightMode(isNight?: false)
    private fun saveUseNightMode(b: Boolean?) = SettingsRepository.instance.putUseNightMode(b?: false)

    fun saveData() {
        saveCompGameMode(complexGameMode)
        saveNightMode(nightTheme)
        saveUseNightMode(useNightTheme)
    }
    fun loadData() {
        complexGameMode = SettingsRepository.instance.readCompGameMode()
        nightTheme = SettingsRepository.instance.readNightMode()
        useNightTheme = SettingsRepository.instance.readUseNightMode()
    }

    override fun hashCode(): Int {
        var result = complexGameMode.hashCode()
        result = 31 * result + nightTheme.hashCode()
        result = 31 * result + useNightTheme.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SettingsState

        if (complexGameMode != other.complexGameMode) return false
        if (nightTheme != other.nightTheme) return false
        if (useNightTheme != other.useNightTheme) return false

        return true
    }
}