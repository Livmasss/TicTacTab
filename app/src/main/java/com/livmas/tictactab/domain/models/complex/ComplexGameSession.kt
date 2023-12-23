package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage

class ComplexGameSession(
    field: ComplexFieldModel,
    current: Player?,
    winner: Player?
): GameSession(field, current, winner) {
    override val _field: IFieldModel
        get() = super._field as ComplexFieldModel
    override fun getCellState(cords: ClassicCoordinatesModel): CellState {
        return when ((_field as ComplexFieldModel)[cords].winner) {
            Player.X -> CellState.X
            Player.O -> CellState.O
            null -> CellState.N
        }
    }

    override val field: ComplexFieldModel
        get() = (_field as ComplexFieldModel).copy()


    override fun makeTurn(cords: ICoordinatesModel): GameMessage {
        cords as ComplexCoordinatesModel
        val state = if (_currentPlayer == Player.X)
            CellState.X
        else
            CellState.O

        cords.apply {
            if ((_field as ComplexFieldModel)[innerCoordinates].field[ClassicCoordinatesModel(x, y)] == CellState.N)
                (_field as ComplexFieldModel)[innerCoordinates].field.set(innerCoordinates, state)
            else
                return GameMessage(
                    null,
                    40
                )
        }

        _winner = when (checkWinner()) {
            CellState.N -> null
            CellState.X -> Player.X
            CellState.O -> Player.O
        }
        _currentPlayer = if (_currentPlayer == Player.X) Player.O else Player.X

        if (_winner != null || _field.isFull()) {
            return GameMessage(
                null,
                when (_winner) {
                    null -> 200
                    Player.X -> 210 + winLineCode
                    Player.O -> 220 + winLineCode
                }
            )
        }
        return GameMessage(null, when(_currentPlayer) {
            Player.X -> 11
            Player.O -> 12
        })
    }
}