package com.livmas.tictactab.ui.fragments.game.sessions.classic

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.livmas.tictactab.GAME_TAG
import com.livmas.tictactab.domain.game_sessions.GameSession
import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.game_sessions.ClassicSession
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage
import com.livmas.tictactab.ui.fragments.game.sessions.GameSessionViewModel
import com.livmas.tictactab.ui.models.enums.Alert

class ClassicGameSessionViewModel : GameSessionViewModel() {

    override val _field: MutableLiveData<IFieldModel> by lazy {
        MutableLiveData(ClassicFieldModel())
    }


    override var session: GameSession? = ClassicSession()

    fun resumeGame() {
        session = ClassicSession(_field.value!! as ClassicFieldModel, _currentPlayer.value, _gameResult.value)
        _field.postValue(session!!.field)
    }
    private fun stopGame() {
        session = null
    }

    override fun restartGame() {
        session = ClassicSession(ClassicFieldModel(), Player.X, null)
        super.restartGame()
    }

    fun makeTurn(cords: ClassicCoordinatesModel) {
        val message = if (session == null || session!!.result != null)
            GameMessage("This game ended! You can restart it.", 31)
        else
            session!!.makeTurn(cords)

        when (message.code) {
            11 -> nextTurn(Player.X)
            12 -> nextTurn(Player.O)
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
            31 -> _alert.postValue(Alert.GameFinished)
            in 30..39 -> {
                _alert.postValue(Alert.SomeError)
            }
            40 -> {
                _alert.postValue(Alert.CellOccupied)
            }
        }

        _lastTurn.postValue(cords)
    }

    private fun nextTurn(currPlayer: Player) {
        _field.postValue(session!!.field)
        _currentPlayer.postValue(currPlayer)
    }
}