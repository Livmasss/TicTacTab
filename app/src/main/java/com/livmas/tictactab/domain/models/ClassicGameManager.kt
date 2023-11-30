package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicGameSession

class ClassicGameManager(private val init_field: ClassicFieldModel) {
    private var game: ClassicGameSession? = null
    val field
        get() = if (game?.field == null) ClassicFieldModel() else game!!.field
    val winner
        get() = game?.winner

    fun startGame() {
        game = ClassicGameSession(init_field)
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