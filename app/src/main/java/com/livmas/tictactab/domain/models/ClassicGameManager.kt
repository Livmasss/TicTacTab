package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicGameSession

class ClassicGameManager {
    private var game: ClassicGameSession? = null

    fun startGame() {
        game = ClassicGameSession()
    }
    fun stopGame() {
        game = null
    }
    fun makeTurn(cords: ClassicCoordinatesModel) {
        game?.let { game ->
            val isFinished =  game.makeTurn(cords)
            if (isFinished)
                stopGame()
        }
    }
}