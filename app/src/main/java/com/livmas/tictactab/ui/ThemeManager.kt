package com.livmas.tictactab.ui

import androidx.appcompat.app.AppCompatDelegate

class ThemeManager {
    companion object {
        var useTheme = false

        fun setTheme(isNight: Boolean) {
            if (useTheme)
                when (isNight) {
                    true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}