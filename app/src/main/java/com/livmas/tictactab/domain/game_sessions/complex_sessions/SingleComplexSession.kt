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

    val currentBlockCords: ClassicCoordinatesModel?
        get() = _currentBlockCords?.copy()
    private var _currentBlockCords: ClassicCoordinatesModel? = null

    override fun postTurnProcess(cords: ICoordinatesModel) {
        _currentBlockCords = ClassicCoordinatesModel(cords)
        super.postTurnProcess(cords)
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

    override fun onBlockClose(cords: ICoordinatesModel) {
        super.onBlockClose(cords)
        _currentBlockCords = null
    }
}