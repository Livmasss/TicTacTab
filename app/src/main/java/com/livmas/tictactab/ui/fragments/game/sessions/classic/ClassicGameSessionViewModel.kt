package com.livmas.tictactab.ui.fragments.game.sessions.classic

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.livmas.tictactab.domain.models.ClassicGameManager
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicGameSession
import com.livmas.tictactab.domain.models.enums.Player

class ClassicGameSessionViewModel : ViewModel() {
    val field: MutableLiveData<ClassicFieldModel> by lazy {
        MutableLiveData<ClassicFieldModel>(ClassicFieldModel())
    }
    val currentPlayer: MutableLiveData<Player> by lazy {
        MutableLiveData<Player>(Player.X)
    }
    val winner: MutableLiveData<Player?> by lazy {
        MutableLiveData<Player?>(null)
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
    }
    //Returns true if game
    fun makeTurn(cords: ClassicCoordinatesModel) {
        val isFinished = gameManager.makeTurn(cords)
        Log.d("winner", gameManager.winner.toString())
        field.value = gameManager.field
        currentPlayer.postValue(gameManager.currentPlayer)

        if (gameManager.winner != null)
            winner.postValue(gameManager.winner)
        if (isFinished)
            stopGame()
    }
}