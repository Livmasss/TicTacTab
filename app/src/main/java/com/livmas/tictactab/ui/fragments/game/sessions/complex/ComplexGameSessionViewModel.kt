package com.livmas.tictactab.ui.fragments.game.sessions.complex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.livmas.tictactab.domain.models.complex.ComplexFieldModel
import com.livmas.tictactab.domain.models.complex.ComplexGameSession
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.fragments.game.sessions.GameSessionViewModel

class ComplexGameSessionViewModel : GameSessionViewModel() {

    val field: LiveData<ComplexFieldModel>
        get() = _field
    private val _field: MutableLiveData<ComplexFieldModel> by lazy {
        MutableLiveData<ComplexFieldModel>(ComplexFieldModel())
    }
    private var session: ComplexGameSession? = ComplexGameSession()

    fun resumeGame() {
        session = ComplexGameSession(field.value!!, _currentPlayer.value, _gameResult.value)
        _field.postValue(session!!.field)
    }
    private fun stopGame() {
        session = null
    }
    fun restartGame() {
        session = ComplexGameSession(ComplexFieldModel(), Player.X, null)

        _field.postValue(session!!.field)
        _currentPlayer.postValue(Player.X)
        _gameResult.postValue(null)
        _winLineCode.postValue(0)
        _alert.postValue(null)
    }

}