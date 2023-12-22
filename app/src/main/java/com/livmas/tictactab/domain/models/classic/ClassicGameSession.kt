package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage

class ClassicGameSession(
    override val _field: ClassicFieldModel,
    current: Player?,
    override var _winner: Player?
): GameSession<ClassicFieldModel, ClassicCoordinatesModel>(_field, current, _winner) {

    override var _currentPlayer = current ?: Player.X
    override var winLineCode = 0
    val winner: Player?
        get() = _winner

    constructor() : this(ClassicFieldModel(), Player.X, null)

    override val field: ClassicFieldModel
        get() = _field.copy()

    override fun makeTurn(cords: ClassicCoordinatesModel): GameMessage {
        val state = if (_currentPlayer == Player.X)
            CellState.X
        else
            CellState.O

        if (_field[cords] == CellState.N)
            _field.set(cords, state)
        else
            return GameMessage(
                null,
                40
            )

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

    override fun getCellState(cords: ClassicCoordinatesModel): CellState {
        return _field[cords]
    }
}