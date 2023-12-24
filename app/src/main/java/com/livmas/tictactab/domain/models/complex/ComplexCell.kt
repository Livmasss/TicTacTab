package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.Cell
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.enums.CellState

class ComplexCell(val field: ClassicFieldModel = ClassicFieldModel(), state: CellState = CellState.N): Cell(state) {
    fun makeTurn(cords: ClassicCoordinatesModel) {
        field
    }
}