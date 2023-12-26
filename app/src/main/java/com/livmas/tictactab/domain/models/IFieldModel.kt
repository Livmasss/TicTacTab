package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel


interface IFieldModel {
    fun set(cords: ClassicCoordinatesModel, value: CellModel)
    operator fun get(cords: ClassicCoordinatesModel): CellModel
    fun isFull(): Boolean
}
