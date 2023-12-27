package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage
import java.util.Stack

class ComplexGameSession(
    field: ComplexFieldModel,
    current: Player?,
    result: GameResult?
): GameSession(field, current, result) {
    constructor() : this(ComplexFieldModel(), Player.X, null)

    override val _field: ComplexFieldModel
        get() = super._field as ComplexFieldModel
    override val field: ComplexFieldModel
        get() = _field.copy()

    val currentPlayer: Player
        get() = _currentPlayer
    private val backStack = Stack<ClassicCoordinatesModel>()

    override fun makeTurn(cords: ICoordinatesModel): GameMessage {
        return field[ClassicCoordinatesModel(cords.x, cords.y)]
            .makeTurn((cords as ComplexCoordinatesModel).innerCoordinates, _currentPlayer).let {
                when (it.code) {
                    in 10..19 -> {
                        changePlayer()
                        it
                    }
                    in 50..59 -> {
                        changePlayer()
                        val c = it.code + cords.y*3 + cords.x
                        GameMessage(it.content, c)
                    }
                    else -> it
                }
            }
    }
}