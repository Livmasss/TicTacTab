package com.livmas.tictactab.ui.fragments.game.sessions.classic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.livmas.tictactab.domain.models.ClassicGameManager
import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicGameSession
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.fragments.game.sessions.GameSessionViewModel
import com.livmas.tictactab.ui.models.enums.Alert

class ClassicGameSessionViewModel : GameSessionViewModel() {

    val field: LiveData<ClassicFieldModel>
        get() = _field
    private val _field: MutableLiveData<ClassicFieldModel> by lazy {
        MutableLiveData<ClassicFieldModel>(ClassicFieldModel())
    }

    private val gameManager = ClassicGameManager()

    fun startGame() {
        gameManager.startGame(
            ClassicGameSession(field.value!!, _currentPlayer.value, when(_gameResult.value) {
                GameResult.X -> Player.X
                GameResult.O -> Player.O
                else -> null
            }),
            null
        )
        _field.value = gameManager.field
    }
    private fun stopGame() {
        gameManager.stopGame()
    }
    fun restartGame() {
        gameManager.startGame(ClassicGameSession(), true)
        _field.value = gameManager.field
        _currentPlayer.postValue(Player.X)

        _gameResult.postValue(null)
        _winLineCode.postValue(0)
        _alert.postValue(null)
    }

    fun makeTurn(cords: ClassicCoordinatesModel) {
        val message = gameManager.makeTurn(cords)

        when (message.code) {
            11 -> nextTurn(Player.X)
            12 -> nextTurn(Player.O)
            in 200..299 -> {
                _field.value = gameManager.field
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

            31 -> _alert.postValue(Alert.GameFinished)
            in 30..39 -> {
                _alert.postValue(Alert.SomeError)
            }
            40 -> {
                _alert.postValue(Alert.CellOccupied)
            }
        }
    }

    private fun nextTurn(currPlayer: Player) {
        _field.value = gameManager.field
        _currentPlayer.postValue(currPlayer)
    }
}