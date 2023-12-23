package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player

abstract class GameSession(
    protected open val _field: IFieldModel,
    current: Player?,
    protected open var _result: GameResult?
): IGameSession {

    companion object {
        const val TAG = "classic_game"
    }
    abstract val field: IFieldModel

    protected open var _currentPlayer = current ?: Player.X
    protected open var winLineCode = 0

    protected abstract fun getCellState(cords: ClassicCoordinatesModel): CellState

    private fun checkLine(cell1: CellState, cell2: CellState, cell3: CellState): Boolean {
        return cell1 != CellState.N && cell1 == cell2 && cell2 == cell3
    }

    protected fun checkWinner(): CellState {
        return if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 0)),
                getCellState(ClassicCoordinatesModel(0, 1)),
                getCellState(ClassicCoordinatesModel(0, 2)))
        ) {
            winLineCode = 1
            getCellState(ClassicCoordinatesModel(0, 0))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(1, 0)),
                getCellState(ClassicCoordinatesModel(1, 1)),
                getCellState(ClassicCoordinatesModel(1, 2)))
        ) {
            winLineCode = 2
            getCellState(ClassicCoordinatesModel(1, 0))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(2, 0)),
                getCellState(ClassicCoordinatesModel(2, 1)),
                getCellState(ClassicCoordinatesModel(2, 2)))
        ) {
            winLineCode = 3
            getCellState(ClassicCoordinatesModel(2, 0))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 0)),
                getCellState(ClassicCoordinatesModel(1, 1)),
                getCellState(ClassicCoordinatesModel(2, 2)))
        ) {
            winLineCode = 4
            getCellState(ClassicCoordinatesModel(0, 0))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 2)),
                getCellState(ClassicCoordinatesModel(1, 1)),
                getCellState(ClassicCoordinatesModel(2, 0)))
        ) {
            winLineCode = 5
            getCellState(ClassicCoordinatesModel(0, 2))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 0)),
                getCellState(ClassicCoordinatesModel(1, 0)),
                getCellState(ClassicCoordinatesModel(2, 0)))
        ) {
            winLineCode = 6
            getCellState(ClassicCoordinatesModel(0, 0))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 1)),
                getCellState(ClassicCoordinatesModel(1, 1)),
                getCellState(ClassicCoordinatesModel(2, 1)))
        ) {
            winLineCode = 7
            getCellState(ClassicCoordinatesModel(0, 1))
        }
        else if (checkLine(
                getCellState(ClassicCoordinatesModel(0, 2)),
                getCellState(ClassicCoordinatesModel(1, 2)),
                getCellState(ClassicCoordinatesModel(2, 2)))
        ) {
            winLineCode = 8
            getCellState(ClassicCoordinatesModel(0, 2))
        }
        else {
            winLineCode = 0
            CellState.N
        }
    }
}
