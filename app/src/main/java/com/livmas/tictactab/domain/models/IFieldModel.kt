package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState


interface IFieldModel {
    fun set(cords: ClassicCoordinatesModel, value: CellModel)
    fun set(cords: ICoordinatesModel, value: CellState)
    operator fun get(cords: ClassicCoordinatesModel): CellModel
    operator fun get(cords: ICoordinatesModel): CellState
    fun isFull(): Boolean
}
