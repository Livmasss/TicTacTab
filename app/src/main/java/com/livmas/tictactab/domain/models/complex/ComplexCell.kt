package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.CellModel
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage

class ComplexCell(val field: ClassicFieldModel = ClassicFieldModel(), state: CellState = CellState.N): CellModel(null) {
    fun makeTurn(cords: ICoordinatesModel, player: Player): GameMessage {
        if (state != null)
            return GameMessage(
                null,
                41
            )

        val newState = if (player == Player.X)
            CellState.X
        else
            CellState.O

        if (field[cords as ClassicCoordinatesModel].state == CellState.N)
            field.setState(cords, newState)
        else
            return GameMessage(
                null,
                40
            )
        state = when (checkWinner()) {
            CellState.N -> if (field.isFull()) CellState.N else null
            CellState.X -> CellState.X
            CellState.O -> CellState.O
        }

        return GameMessage(null,
            when (state) {
                CellState.N -> 50
                CellState.X -> 51
                CellState.O -> 52
                null -> 10
            }
        )
    }
    private fun getCellState(cords: ClassicCoordinatesModel): CellState {
        return field[cords].state ?: CellState.N
    }

    private fun checkLine(cell1: CellState, cell2: CellState, cell3: CellState): Boolean {
        return cell1 != CellState.N && cell1 == cell2 && cell2 == cell3
    }

    fun checkWinner(): CellState {
        return if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 0)),
                getCellState(ClassicCoordinatesModel(0, 1)),
                getCellState(ClassicCoordinatesModel(0, 2)))
        ) {
            getCellState(ClassicCoordinatesModel(0, 0))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(1, 0)),
                getCellState(ClassicCoordinatesModel(1, 1)),
                getCellState(ClassicCoordinatesModel(1, 2)))
        ) {
            getCellState(ClassicCoordinatesModel(1, 0))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(2, 0)),
                getCellState(ClassicCoordinatesModel(2, 1)),
                getCellState(ClassicCoordinatesModel(2, 2)))
        ) {
            getCellState(ClassicCoordinatesModel(2, 0))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 0)),
                getCellState(ClassicCoordinatesModel(1, 1)),
                getCellState(ClassicCoordinatesModel(2, 2)))
        ) {
            getCellState(ClassicCoordinatesModel(0, 0))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 2)),
                getCellState(ClassicCoordinatesModel(1, 1)),
                getCellState(ClassicCoordinatesModel(2, 0)))
        ) {
            getCellState(ClassicCoordinatesModel(0, 2))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 0)),
                getCellState(ClassicCoordinatesModel(1, 0)),
                getCellState(ClassicCoordinatesModel(2, 0)))
        ) {
            getCellState(ClassicCoordinatesModel(0, 0))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 1)),
                getCellState(ClassicCoordinatesModel(1, 1)),
                getCellState(ClassicCoordinatesModel(2, 1)))
        ) {
            getCellState(ClassicCoordinatesModel(0, 1))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 2)),
                getCellState(ClassicCoordinatesModel(1, 2)),
                getCellState(ClassicCoordinatesModel(2, 2)))
        ) {
            getCellState(ClassicCoordinatesModel(0, 2))
        }
        else {
            CellState.N
        }
    }
}