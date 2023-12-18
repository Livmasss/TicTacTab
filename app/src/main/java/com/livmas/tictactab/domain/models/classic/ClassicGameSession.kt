package com.livmas.tictactab.domain.models.classic

import android.util.Log
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.domain.models.exceptions.CellOccupiedException

class ClassicGameSession(init_field: ClassicFieldModel, current: Player?) {
    private val _field = init_field
    private var _currentPlayer = current ?: Player.X
    private var _winner: Player? = null

    companion object {
        const val TAG = "classic_game"
    }

    constructor() : this(ClassicFieldModel(), Player.X)

    val winner: Player?
        get() = _winner
    val currentPlayer: Player
        get() = _currentPlayer
    val field: ClassicFieldModel
        get() = _field.copy()

    //Returns true if game finished after this turn
    fun makeTurn(cords: ClassicCoordinatesModel): Boolean {
        try {
            _field.makeTurn(ClassicTurnModel(cords, _currentPlayer))
        }
        catch (e: CellOccupiedException) {
            return false
        }

        _winner = when (checkWinner()) {
            CellState.N -> null
            CellState.X -> Player.X
            CellState.O -> Player.O
        }
        _currentPlayer = if (_currentPlayer == Player.X) Player.O else Player.X

        Log.d(TAG, _field.isFull().toString())
        return _winner != null || _field.isFull()
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