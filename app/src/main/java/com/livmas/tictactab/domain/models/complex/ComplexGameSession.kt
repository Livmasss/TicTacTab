package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.ICoordinatesModel
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
    constructor() : this(ComplexFieldModel(), Player.X, null)

    override val _field: ComplexFieldModel
        get() = super._field as ComplexFieldModel
    override val field: ComplexFieldModel
        get() = _field.copy()

    override fun getCellState(cords: ClassicCoordinatesModel): CellState {
        return _field[cords].state
    }

    override fun makeTurn(cords: ICoordinatesModel): GameMessage {
        cords as ComplexCoordinatesModel
        val state = if (_currentPlayer == Player.X)
            CellState.X
        else
            CellState.O

        cords.apply {
            _field[ClassicCoordinatesModel(x, y)].makeTurn(innerCoordinates)
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