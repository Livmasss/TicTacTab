package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage

abstract class GameSession(
    protected open val _field: IFieldModel,
    current: Player?,
    protected open var _result: GameResult?
): IGameSession {

    companion object {
        const val TAG = "classic_game"
    }
    abstract val field: IFieldModel

    val result: GameResult?
        get() = _result

    val currentPlayer: Player
        get() = _currentPlayer
    protected open var _currentPlayer = current ?: Player.X
    protected open var winLineCode = 0

    private fun getCellState(cords: ClassicCoordinatesModel): CellState {
        return _field[cords].state ?: CellState.N
    }

    protected fun changePlayer() {
        _currentPlayer = if (_currentPlayer == Player.X) Player.O else Player.X
    }

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

    override fun makeTurn(cords: ICoordinatesModel): GameMessage {
        val state = if (_currentPlayer == Player.X)
            CellState.X
        else
            CellState.O

        preTurnProcess(cords).let {
            when (it.code) {
                in 40..49 -> return it
                else -> {}
            }
        }

        _field.set(cords, state)

        postTurnProcess(cords)
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

    protected open fun preTurnProcess(cords: ICoordinatesModel) : GameMessage {
        return GameMessage(
            null,
            0
        )
    }

    protected open fun postTurnProcess(cords: ICoordinatesModel) {
        _result = when (checkWinner()) {
            CellState.N -> if (_field.isFull()) GameResult.N else null
            CellState.X -> GameResult.X
            CellState.O -> GameResult.O
        }
    }
}
