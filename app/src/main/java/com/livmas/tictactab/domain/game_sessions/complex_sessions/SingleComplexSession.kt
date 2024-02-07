package com.livmas.tictactab.domain.game_sessions.complex_sessions

import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexFieldModel
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage

class SingleComplexSession(
    field: ComplexFieldModel,
    current: Player?,
    result: GameResult?
): BasicComplexSession(field, current, result) {
    constructor() : this(ComplexFieldModel(), Player.X, null)

    override fun postTurnProcess(cords: ICoordinatesModel) {
        super.postTurnProcess(cords)

        _currentBlockCords = ClassicCoordinatesModel(cords)
        if (_field[ClassicCoordinatesModel(cords)].state != null)
            _currentBlockCords = null
    }

    override fun preTurnProcess(cords: ICoordinatesModel): GameMessage {
        if (_currentBlockCords == null)
            return super.preTurnProcess(cords)

        return if (_currentBlockCords != (ClassicCoordinatesModel(cords)))
            GameMessage(
                null,
                42
            )
        else
            super.preTurnProcess(cords)
    }
}