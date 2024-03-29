package com.livmas.tictactab.data.sources

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.livmas.tictactab.SETTINGS_TAG
import com.livmas.tictactab.data.sources.SettingsSharedPreferencesDataSource.Companion.PREF_NIGHT_MODE

class SettingsSharedPreferencesDataSource {
    companion object {
        const val SP_NAME = "com.livmas.tictactab"
        const val PREF_GAME_MODE = "comp_game_mode"
        const val PREF_NIGHT_MODE = "night_mode"
        const val PREF_USE_NIGHT_MODE = "use_night_mode"

        val instance: SettingsSharedPreferencesDataSource by lazy {
            SettingsSharedPreferencesDataSource()
        }
    }
    private var sp: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    fun initiate(context: Context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        editor = sp?.edit()
    }

    fun putCompGameMode(gameMode: Int) {
        if (isEditorNull())
            return
        editor!!.putInt(PREF_GAME_MODE, gameMode)
        editor!!.commit()
    }

    fun putNightMode(isNight: Boolean) {
        if (isEditorNull())
            return
        editor!!.putBoolean(PREF_NIGHT_MODE, isNight)
        editor!!.commit()
    }

    fun putUseNightMode(b: Boolean) {
        if (isEditorNull())
            return
        Log.d(SETTINGS_TAG, "Use night mode put in prefs: $b")
        editor!!.putBoolean(PREF_USE_NIGHT_MODE, b)
        editor!!.commit()
    }

    fun readCompGameMode(): Int {
        return if (isSharedPrefNull())
            0
        else
            sp!!.getInt(PREF_GAME_MODE, 0)
    }

    fun readNightMode(): Boolean {
        return if (isSharedPrefNull())
            false
        else
            sp!!.getBoolean(PREF_NIGHT_MODE, false)
    }

    fun readUseNightMode(): Boolean {
        return if (isSharedPrefNull())
            false
        else
            sp!!.getBoolean(PREF_USE_NIGHT_MODE, false)
    }

    private fun isEditorNull(): Boolean {
        return (editor == null).let {
            if (it)
                Log.w(SETTINGS_TAG, "SharedPreferences editor object is null!")
            it
        }
    }

    private fun isSharedPrefNull(): Boolean {
        return (sp == null).let {
            if (it)
                Log.w(SETTINGS_TAG, "SharedPreferences object is null!")
            it
        }
    }
}