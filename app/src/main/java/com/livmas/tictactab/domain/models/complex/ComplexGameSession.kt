package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState
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

    private val backStack = Stack<ClassicCoordinatesModel>()

    override fun postTurnProcess(cords: ICoordinatesModel) {
        val fCell = _field[ClassicCoordinatesModel(cords)]
        fCell.state = when (fCell.checkWinner()) {
            CellState.N -> if (field.isFull()) CellState.N else null
            CellState.X -> CellState.X
            CellState.O -> CellState.O
        }

        super.postTurnProcess(cords)
    }
    override fun preTurnProcess(cords: ICoordinatesModel): GameMessage {
        cords as ComplexCoordinatesModel
        if (_field[cords] != CellState.N)
            return GameMessage(
                null,
                40
            )

        if (_field[ClassicCoordinatesModel(cords)].state != null)
            return GameMessage(
                null,
                41
            )

        return super.preTurnProcess(cords)
    }
    override fun makeTurn(cords: ICoordinatesModel): GameMessage {
        val message = super.makeTurn(cords)
        val state = _field[ClassicCoordinatesModel(cords)].state

        if (message.code !in 10..19)
            return message

        return GameMessage(null,
            when (state) {
                CellState.N -> 50
                CellState.X -> 51
                CellState.O -> 52
                null -> return message
            }
        )
    }
}