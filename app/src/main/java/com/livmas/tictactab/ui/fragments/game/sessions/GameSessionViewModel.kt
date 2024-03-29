package com.livmas.tictactab.ui.fragments.game.sessions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.livmas.tictactab.domain.game_sessions.GameSession
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.models.enums.Alert

abstract class GameSessionViewModel: ViewModel() {
    protected abstract var session: GameSession?

    val lastTurn: LiveData<ICoordinatesModel?>
        get() = _lastTurn
    protected val _lastTurn: MutableLiveData<ICoordinatesModel?> by lazy {
        MutableLiveData(null)
    }
    val field: LiveData<IFieldModel>
        get() = _field
    protected abstract val _field: MutableLiveData<IFieldModel>
    val currentPlayer: LiveData<Player>
        get() = _currentPlayer
    protected val _currentPlayer: MutableLiveData<Player> by lazy {
        MutableLiveData<Player>(Player.X)
    }
    val gameResult: LiveData<GameResult?>
        get() = _gameResult
    protected val _gameResult: MutableLiveData<GameResult?> by lazy {
        MutableLiveData<GameResult?>(null)
    }
    val alert: LiveData<Alert?>
        get() = _alert
    protected val _alert: MutableLiveData<Alert?> by lazy {
        MutableLiveData<Alert?>(null)
    }
    val winLineCode: MutableLiveData<Int>
        get() = _winLineCode
    protected val _winLineCode: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }

    fun clearAlert() {
        _alert.postValue(null)
    }

    open fun restartGame() {
        _field.postValue(session!!.field)
        _lastTurn.postValue(null)
        _currentPlayer.postValue(Player.X)
        _gameResult.postValue(null)
        _winLineCode.postValue(0)
        _alert.postValue(null)
    }
}