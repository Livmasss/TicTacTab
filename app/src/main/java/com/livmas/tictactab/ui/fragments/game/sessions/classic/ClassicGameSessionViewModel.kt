package com.livmas.tictactab.ui.fragments.game.sessions.classic

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.livmas.tictactab.domain.models.ClassicGameManager
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicGameSession
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player

class ClassicGameSessionViewModel : ViewModel() {
    val field: MutableLiveData<ClassicFieldModel> by lazy {
        MutableLiveData<ClassicFieldModel>(ClassicFieldModel())
    }
    val currentPlayer: MutableLiveData<Player> by lazy {
        MutableLiveData<Player>(Player.X)
    }
    val gameResult: MutableLiveData<GameResult?> by lazy {
        MutableLiveData<GameResult?>(null)
    }
    val alert: MutableLiveData<String?> by lazy {
        MutableLiveData<String?>(null)
    }
    val winLineCode: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }

    private val gameManager = ClassicGameManager()

    fun startGame() {
        gameManager.startGame(ClassicGameSession(field.value!!, currentPlayer.value))
        field.value = gameManager.field
    }
    fun stopGame() {
        gameManager.stopGame()
    }
    fun restartGame() {
        gameManager.startGame(ClassicGameSession())
        field.value = gameManager.field
        currentPlayer.postValue(Player.X)

        gameResult.postValue(null)
        winLineCode.postValue(0)
        alert.postValue(null)
//        winner.postValue(null)
//        gameFinished.postValue(false)
    }
    //Returns true if game
    fun makeTurn(cords: ClassicCoordinatesModel) {
        val message = gameManager.makeTurn(cords)
        field.value = gameManager.field
        currentPlayer.postValue(gameManager.currentPlayer)

        when (message.code) {
            in 200..299 -> {
                Log.i(ClassicGameSession.TAG, "Game finished with code ${message.code}")
//                winner.postValue(gameManager.winner)
//                gameFinished.postValue(true)
                winLineCode.postValue(message.code % 10)
                val winnerCode = (message.code / 10) % 10
                gameResult.postValue(
                    when (winnerCode) {
                        0 -> GameResult.N
                        1 -> GameResult.X
                        2 -> GameResult.O
                        else -> null
                    }
                )

                stopGame()
            }

            in 30..39 -> {
                alert.postValue(message.content)
            }
            40 -> {
                alert.postValue(message.content)
            }
        }
    }

    fun clearAlert() {
        alert.postValue(null)
    }
}