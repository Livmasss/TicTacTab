package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage

class ComplexGameSession(
    field: ComplexFieldModel,
    current: Player?,
    result: GameResult?
): GameSession(field, current, result) {
    override val _field: IFieldModel
        get() = super._field as ComplexFieldModel
    override fun getCellState(cords: ClassicCoordinatesModel): CellState {
        return when ((_field as ComplexFieldModel)[cords].result) {
            GameResult.X -> CellState.X
            GameResult.O -> CellState.O
            else -> CellState.N
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

        _result = when (checkWinner()) {
            CellState.N -> if (_field.isFull()) GameResult.N else null
            CellState.X -> GameResult.X
            CellState.O -> GameResult.O
        }
        _currentPlayer = if (_currentPlayer == Player.X) Player.O else Player.X

        return GameMessage(null,
            when (_result) {
                GameResult.N -> 200
                GameResult.X -> 210 + winLineCode
                GameResult.O -> 220 + winLineCode
                null -> when(_currentPlayer) {
                    Player.X -> 11
                    Player.O -> 12
                }
            }
        )
    }
}