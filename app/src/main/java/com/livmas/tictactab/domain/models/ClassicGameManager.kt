package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicGameSession

class ClassicGameManager() {
    private var game: ClassicGameSession? = null
    val field
        get() = if (game?.field == null) ClassicFieldModel() else game!!.field
    val winner
        get() = game?.winner
    val currentPlayer
        get() = game?.currentPlayer

    fun startGame(session: ClassicGameSession) {
        game = session
    }
    fun stopGame() {
        game = null
    }

    //Returns true if game finished after this turn
    fun makeTurn(cords: ClassicCoordinatesModel): Boolean {
        game?.let { game ->
            val isFinished = game.makeTurn(cords)
            return (isFinished)
        }
        return true
    }
}