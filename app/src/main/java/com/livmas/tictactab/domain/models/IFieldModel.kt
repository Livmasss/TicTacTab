package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel


interface IFieldModel {
    fun set(cords: ClassicCoordinatesModel, value: CellValue)
    operator fun get(cords: ClassicCoordinatesModel): CellValue
    fun isFull(): Boolean
}
