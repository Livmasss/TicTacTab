package com.livmas.tictactab.ui.fragments.settings

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.livmas.tictactab.data.repositories.SettingsRepository
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class SettingsViewModel: ViewModel() {

    private val repository = SettingsRepository()

    val complexGameMode: LiveData<ComplexGameMode>
        get() = _complexGameMode
    private val _complexGameMode: MutableLiveData<ComplexGameMode> by lazy {
        MutableLiveData<ComplexGameMode>(repository.readCompGameMode())
    }
    val nightTheme: LiveData<Boolean>
        get() = _nightTheme
    private val _nightTheme: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(repository.readNightMode())
    }

    val firstColor: MutableLiveData<Color>
        get() = _firstColor
    private val _firstColor: MutableLiveData<Color> by lazy {
        MutableLiveData<Color>()
    }

    val secondColor: MutableLiveData<Color>
        get() = _secondColor
    private val _secondColor: MutableLiveData<Color> by lazy {
        MutableLiveData<Color>()
    }

    fun postGameMode(mode: ComplexGameMode) {
        _complexGameMode.postValue(mode)
    }
    fun postNightTheme(isNight: Boolean?) {
        _nightTheme.postValue(isNight)
    }

    fun saveData() {
        repository.apply {
            putCompGameMode(_complexGameMode.value ?: ComplexGameMode.Stack)
            putNightMode(_nightTheme.value?: false)
        }
    }

    fun postUseNightTheme(bool: Boolean) {

    }
}