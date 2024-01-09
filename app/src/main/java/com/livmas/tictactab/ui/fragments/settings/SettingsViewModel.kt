package com.livmas.tictactab.ui.fragments.settings

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class SettingsViewModel: ViewModel() {
    private val complexGameMode: MutableLiveData<ComplexGameMode> by lazy {
        MutableLiveData<ComplexGameMode>(ComplexGameMode.Stack)
    }
    private val firstColor: MutableLiveData<Color> by lazy {
        MutableLiveData<Color>()
    }
    private val secondColor: MutableLiveData<Color> by lazy {
        MutableLiveData<Color>()
    }
}