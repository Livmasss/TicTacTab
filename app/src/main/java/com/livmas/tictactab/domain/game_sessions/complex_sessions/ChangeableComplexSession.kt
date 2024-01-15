package com.livmas.tictactab.domain.game_sessions.complex_sessions

import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexFieldModel
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player

abstract class ChangeableComplexSession(
    field: ComplexFieldModel,
    current: Player?,
    result: GameResult?
): BasicComplexSession(field, current, result) {

    override fun postTurnProcess(cords: ICoordinatesModel) {
        super.postTurnProcess(cords)

        cords as ComplexCoordinatesModel
        cords.innerCoordinates.also {
            _currentBlockCords =
                if (_field[it].state == null)
                    it
                else
                    onChooseClosedBlock()
        }
    }
    abstract fun onChooseClosedBlock(): ClassicCoordinatesModel?
}