package com.livmas.tictactab.ui.fragments.settings

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class SettingsViewModel: ViewModel() {
    val complexGameMode: LiveData<ComplexGameMode>
        get() = _complexGameMode
    private val _complexGameMode: MutableLiveData<ComplexGameMode> by lazy {
        MutableLiveData<ComplexGameMode>(ComplexGameMode.Stack)
    }
    val nightTheme: LiveData<Boolean>
        get() = _nightTheme
    private val _nightTheme: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
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
}