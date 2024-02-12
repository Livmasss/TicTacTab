package com.livmas.tictactab.ui.fragments.settings

import com.livmas.tictactab.data.repositories.SettingsRepository
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class SettingsState(
    var complexGameMode: ComplexGameMode,
    var nightTheme: Boolean,
) {
    constructor() : this(
        SettingsRepository.instance.readCompGameMode(),
        SettingsRepository.instance.readNightMode(),
    )

    fun saveNightMode() = saveNightMode(nightTheme)
    private fun saveNightMode(isNight: Boolean?) = SettingsRepository.instance.putNightMode(isNight?: false)

    fun loadData() {
        complexGameMode = SettingsRepository.instance.readCompGameMode()
        nightTheme = SettingsRepository.instance.readNightMode()
    }

    override fun hashCode(): Int {
        var result = complexGameMode.hashCode()
        result = 31 * result + nightTheme.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SettingsState

        if (complexGameMode != other.complexGameMode) return false
        if (nightTheme != other.nightTheme) return false

        return true
    }
}