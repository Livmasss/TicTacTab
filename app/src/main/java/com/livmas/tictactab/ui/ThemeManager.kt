package com.livmas.tictactab.ui

import androidx.appcompat.app.AppCompatDelegate

class ThemeManager {
    companion object {
        fun setTheme(isNight: Boolean) {
            if (isNight)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}