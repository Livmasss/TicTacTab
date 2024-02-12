package com.livmas.tictactab.domain.game_sessions.complex_sessions

import com.livmas.tictactab.domain.game_sessions.GameSession
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexFieldModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage

open class BasicComplexSession(
    field: ComplexFieldModel,
    current: Player?,
    result: GameResult?
): GameSession(field, current, result) {
    constructor() : this(ComplexFieldModel(), Player.X, null)

    val currentBlockCords: ClassicCoordinatesModel?
        get() = _currentBlockCords?.copy()
    protected var _currentBlockCords: ClassicCoordinatesModel? = null

    override val _field: ComplexFieldModel
        get() = super._field as ComplexFieldModel
    override val field: ComplexFieldModel
        get() = _field.copy()

    override fun postTurnProcess(cords: ICoordinatesModel) {
        val fCell = _field[ClassicCoordinatesModel(cords)]
        fCell.state = when (fCell.checkWinner()) {
            CellState.N -> if (fCell.field.isFull()) CellState.N else null
            CellState.X -> CellState.X
            CellState.O -> CellState.O
        }

        super.postTurnProcess(cords)
    }
    override fun preTurnProcess(cords: ICoordinatesModel): GameMessage {
        cords as ComplexCoordinatesModel

        //Turn in unavailable block
        if (_currentBlockCords != null && _currentBlockCords != (ClassicCoordinatesModel(cords)))
            return GameMessage(
                null,
                42
            )

        //Closed block
        if (_field[ClassicCoordinatesModel(cords)].state != null)
            return GameMessage(
                null,
                41
            )

        //Cell occupied
        if (_field[cords] != CellState.N)
            return GameMessage(
                null,
                40
            )

        return super.preTurnProcess(cords)
    }
    override fun makeTurn(cords: ICoordinatesModel): GameMessage {
        val message = super.makeTurn(cords)
        val state = _field[ClassicCoordinatesModel(cords)].state

        if (message.code !in 10..19)
            return message

        return GameMessage(null,
            when (state) {
                CellState.N -> 50
                CellState.X -> 51
                CellState.O -> 52
                null -> return message
            }
        )
    }
}