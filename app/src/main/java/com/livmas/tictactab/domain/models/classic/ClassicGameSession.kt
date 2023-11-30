package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.domain.models.exceptions.CellOccupiedException

class ClassicGameSession(init_field: ClassicFieldModel) {
    private val _field = init_field
    val field: ClassicFieldModel
        get() = _field.copy()


    private var currentPlayer = Player.X
    private var winner: Player? = null

    fun makeTurn(cords: ClassicCoordinatesModel): Boolean {
        try {
            _field.makeTurn(ClassicTurnModel(cords, currentPlayer))
        }
        catch (e: CellOccupiedException) {
            return false
        }

        winner = when (checkWinner()) {
            CellState.N -> null
            CellState.X -> Player.X
            CellState.O -> Player.O
        }
        currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X

        return winner != null
    }

    private fun checkWinner(): CellState {
        return if (_field[0, 0] != CellState.N && _field[0, 0] == _field[0, 1] && _field[0, 1] == _field[0, 2])
            _field[0, 0]
        else if (_field[1, 0] != CellState.N && _field[1, 0] == _field[1, 1] && _field[1, 1] == _field[1, 2])
            _field[1, 0]
        else if (_field[2, 0] != CellState.N && _field[2, 0] == _field[2, 1] && _field[2, 1] == _field[2, 2])
            _field[2, 0]
        else if (_field[0, 0] != CellState.N && _field[0, 0] == _field[1, 1] && _field[1, 1] == _field[2, 2])
            _field[0, 0]
        else if (_field[0, 2] != CellState.N && _field[0, 2] == _field[1, 1] && _field[1, 1] == _field[2, 0])
            _field[0, 2]
        else if (_field[0, 0] != CellState.N && _field[0, 0] == _field[1, 0] && _field[1, 0] == _field[2, 0])
            _field[0, 0]
        else if (_field[0, 1] != CellState.N && _field[0, 1] == _field[1, 1] && _field[1, 1] == _field[2, 1])
            _field[0, 1]
        else if (_field[0, 2] != CellState.N && _field[0, 2] == _field[1, 2] && _field[1, 2] == _field[2, 2])
            _field[0, 2]
        else
            CellState.N
    }
}