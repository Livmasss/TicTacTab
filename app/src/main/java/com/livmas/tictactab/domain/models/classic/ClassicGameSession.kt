package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player

class ClassicGameSession{
    private val field = ClassicFieldModel()
    private var currentPlayer = Player.X
    private var winner: Player? = null

    fun makeTurn(cords: ClassicCoordinatesModel): Boolean {
        field.makeTurn(ClassicTurnModel(cords, currentPlayer))

        winner = when (checkWinner()) {
            CellState.N -> null
            CellState.X -> Player.X
            CellState.O -> Player.O
        }

        return winner != null
    }

    private fun checkWinner(): CellState {
        return if (field[0, 0] != CellState.N && field[0, 0] == field[0, 1] && field[0, 1] == field[0, 2])
            field[0, 0]
        else if (field[1, 0] != CellState.N && field[1, 0] == field[1, 1] && field[1, 1] == field[1, 2])
            field[1, 0]
        else if (field[2, 0] != CellState.N && field[2, 0] == field[2, 1] && field[2, 1] == field[2, 2])
            field[2, 0]
        else if (field[0, 0] != CellState.N && field[0, 0] == field[1, 1] && field[1, 1] == field[2, 2])
            field[0, 0]
        else if (field[0, 2] != CellState.N && field[0, 2] == field[1, 1] && field[1, 1] == field[2, 0])
            field[0, 2]
        else if (field[0, 0] != CellState.N && field[0, 0] == field[1, 0] && field[1, 0] == field[2, 0])
            field[0, 0]
        else if (field[0, 1] != CellState.N && field[0, 1] == field[1, 1] && field[1, 1] == field[2, 1])
            field[0, 1]
        else if (field[0, 2] != CellState.N && field[0, 2] == field[1, 2] && field[1, 2] == field[2, 2])
            field[0, 2]
        else
            CellState.N
    }
}