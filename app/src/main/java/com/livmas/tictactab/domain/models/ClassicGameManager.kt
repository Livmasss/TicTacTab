package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicGameSession

class ClassicGameManager(private val init_field: ClassicFieldModel) {
    private var game: ClassicGameSession? = null
    private val _field: ClassicFieldModel
        get() = if (game?.field == null) ClassicFieldModel() else game!!.field
    val field
        get() = _field.copy()

    fun startGame() {
        game = ClassicGameSession(init_field)
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