package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player

class ComplexGameSession(
    field: ComplexFieldModel,
    current: Player?,
    result: GameResult?
): GameSession(field, current, result) {
    constructor() : this(ComplexFieldModel(), Player.X, null)

    override val _field: ComplexFieldModel
        get() = super._field as ComplexFieldModel
    override val field: ComplexFieldModel
        get() = _field.copy()

    override fun getCellState(cords: ClassicCoordinatesModel): CellState {
        return _field[cords].state
    }

    override fun makeTurn(cords: ICoordinatesModel)  = field[ClassicCoordinatesModel(cords.x, cords.y)]
        .makeTurn(cords as ComplexCoordinatesModel, _currentPlayer)
}