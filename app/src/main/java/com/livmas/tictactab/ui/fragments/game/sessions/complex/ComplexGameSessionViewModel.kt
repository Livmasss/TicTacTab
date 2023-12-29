package com.livmas.tictactab.ui.fragments.game.sessions.complex

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.complex.ComplexCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexFieldModel
import com.livmas.tictactab.domain.models.complex.ComplexGameSession
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage
import com.livmas.tictactab.ui.fragments.game.sessions.GameSessionViewModel
import com.livmas.tictactab.ui.models.enums.Alert

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

    fun makeTurn(cords: ComplexCoordinatesModel) {
        val message = if (session == null || session!!.result != null)
            GameMessage("This game ended! You can restart it.", 31)
        else
            session!!.makeTurn(cords)


        when (message.code) {
            in 10..19 -> nextTurn()
            in 200..299 -> {
                _field.postValue(session!!.field)
                Log.i(GameSession.TAG, "Game finished with code ${message.code}")

                _winLineCode.postValue(message.code % 10)
                val winnerCode = (message.code / 10) % 10
                _gameResult.postValue(
                    when (winnerCode) {
                        0 -> GameResult.N
                        1 -> GameResult.X
                        2 -> GameResult.O
                        else -> null
                    }
                )
                stopGame()
            }
            in 500..599 -> {
                val cellNum = message.code % 10
            }

            31 -> _alert.postValue(Alert.GameFinished)
            in 30..39 -> {
                _alert.postValue(Alert.SomeError)
            }
            40 -> {
                _alert.postValue(Alert.CellOccupied)
            }
            41 -> {
                _alert.postValue(Alert.BlockFinished)
            }
        }
    }

    private fun nextTurn() {
        if (session == null) {
            _alert.postValue(Alert.SomeError)
            return
        }
        _field.postValue(session!!.field)
        _currentPlayer.postValue(session!!.currentPlayer)
    }
}