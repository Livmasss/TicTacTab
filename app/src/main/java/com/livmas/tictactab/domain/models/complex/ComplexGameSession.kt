package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.GameMessage

class ComplexGameSession(
    field: ComplexFieldModel,
    current: Player?,
    winner: Player?
): GameSession<ComplexFieldModel, ComplexCoordinatesModel>(field, current, winner) {
    override fun getCellState(cords: ClassicCoordinatesModel): CellState {
        return when (_field[cords].winner) {
            Player.X -> CellState.X
            Player.O -> CellState.O
            null -> CellState.N
        }
    }

    override val field: ComplexFieldModel
        get() = _field.copy()

    override fun makeTurn(cords: ComplexCoordinatesModel): GameMessage {
        TODO("Not yet implemented")
    }


}