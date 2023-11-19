package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.enums.Player

class ClassicGameSession{
    private val field = ClassicFieldModel()
    private var currentPlayer = Player.X
    private var winner: Player? = null

    fun makeTurn(cords: ClassicCoordinatesModel) {
        field.makeTurn(ClassicTurnModel(cords, currentPlayer))

    }
}