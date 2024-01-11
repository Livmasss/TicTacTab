package com.livmas.tictactab.ui.fragments.game.sessions.complex

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.livmas.tictactab.GAME_TAG
import com.livmas.tictactab.domain.game_sessions.GameSession
import com.livmas.tictactab.domain.game_sessions.complex_sessions.BasicComplexSession
import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.complex.ComplexCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexFieldModel
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage
import com.livmas.tictactab.ui.fragments.game.sessions.GameSessionViewModel
import com.livmas.tictactab.ui.models.enums.Alert

class ComplexGameSessionViewModel : GameSessionViewModel() {

    override val _field: MutableLiveData<IFieldModel> by lazy {
        MutableLiveData(ComplexFieldModel())
    }
    override var session: GameSession? = BasicComplexSession()

    fun resumeGame() {
        session = BasicComplexSession(_field.value!! as ComplexFieldModel, _currentPlayer.value, _gameResult.value)
        _field.postValue(session!!.field)
    }
    private fun stopGame() {
        session = null
    }
    override fun restartGame() {
        session = BasicComplexSession(ComplexFieldModel(), Player.X, null)
        super.restartGame()
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
                Log.i(GAME_TAG, "Game finished with code ${message.code}")

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
            in 50..59 -> nextTurn()

            31 -> _alert.postValue(Alert.GameFinished)
            in 30..39 -> _alert.postValue(Alert.SomeError)
            40 -> _alert.postValue(Alert.CellOccupied)
            41 -> _alert.postValue(Alert.BlockFinished)
            42 -> _alert.postValue(Alert.BlockInactive)
        }
        _lastTurn.postValue(cords)
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