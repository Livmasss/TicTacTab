package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.domain.models.exceptions.CellOccupiedException
import com.livmas.tictactab.ui.GameMessage

class ClassicGameSession(init_field: ClassicFieldModel, current: Player?, winner: Player?) {
    private val _field = init_field
    private var _currentPlayer = current ?: Player.X
    private var _winner: Player? = winner
    private var winLineCode = 0

    companion object {
        const val TAG = "classic_game"
    }

    constructor() : this(ClassicFieldModel(), Player.X, null)

    val field: ClassicFieldModel
        get() = _field.copy()

    //Returns true if game finished after this turn
    fun makeTurn(cords: ClassicCoordinatesModel): GameMessage {
        try {
            _field.makeTurn(ClassicTurnModel(cords, _currentPlayer))
        }
        catch (e: CellOccupiedException) {
            return GameMessage(
                e.message,
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

    private fun checkWinner(): CellState {
        return if (_field[0, 0] != CellState.N && _field[0, 0] == _field[0, 1] && _field[0, 1] == _field[0, 2]) {
            winLineCode = 1
            _field[0, 0]
        }
        else if (_field[1, 0] != CellState.N && _field[1, 0] == _field[1, 1] && _field[1, 1] == _field[1, 2]) {
            winLineCode = 2
            _field[1, 0]
        }
        else if (_field[2, 0] != CellState.N && _field[2, 0] == _field[2, 1] && _field[2, 1] == _field[2, 2]) {
            winLineCode = 3
            _field[2, 0]
        }
        else if (_field[0, 0] != CellState.N && _field[0, 0] == _field[1, 1] && _field[1, 1] == _field[2, 2]) {
            winLineCode = 4
            _field[0, 0]
        }
        else if (_field[0, 2] != CellState.N && _field[0, 2] == _field[1, 1] && _field[1, 1] == _field[2, 0]) {
            winLineCode = 5
            _field[0, 2]
        }
        else if (_field[0, 0] != CellState.N && _field[0, 0] == _field[1, 0] && _field[1, 0] == _field[2, 0]) {
            winLineCode = 6
            _field[0, 0]
        }
        else if (_field[0, 1] != CellState.N && _field[0, 1] == _field[1, 1] && _field[1, 1] == _field[2, 1]) {
            winLineCode = 7
            _field[0, 1]
        }
        else if (_field[0, 2] != CellState.N && _field[0, 2] == _field[1, 2] && _field[1, 2] == _field[2, 2]) {
            winLineCode = 8
            _field[0, 2]
        }
        else {
            winLineCode = 0
            CellState.N
        }
    }
}