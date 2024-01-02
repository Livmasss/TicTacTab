package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage

class ClassicGameSession(
    override val _field: ClassicFieldModel,
    current: Player?,
    override var _result: GameResult?
): GameSession(_field, current, _result) {

    constructor() : this(ClassicFieldModel(), Player.X, null)

    override var _currentPlayer = current ?: Player.X
    override var winLineCode = 0
    override val field: ClassicFieldModel
        get() = _field.copy()

    override fun preTurnProcess(cords: ICoordinatesModel): GameMessage {
        if (_field[ClassicCoordinatesModel(cords)].state != null)
            return GameMessage(
                null,
                40
            )

        return super.preTurnProcess(cords)
    }
    override fun makeTurn(cords: ICoordinatesModel): GameMessage {
        val state = if (_currentPlayer == Player.X)
            CellState.X
        else
            CellState.O

        if (_field[cords as ClassicCoordinatesModel].state == CellState.N)
            _field.setState(cords, state)
        else
            return GameMessage(
                null,
                40
            )

        _result = when (checkWinner()) {
            CellState.N -> if (_field.isFull()) GameResult.N else null
            CellState.X -> GameResult.X
            CellState.O -> GameResult.O
        }
        changePlayer()

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