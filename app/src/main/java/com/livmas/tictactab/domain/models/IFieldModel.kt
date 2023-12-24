package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel


interface IFieldModel {
    fun set(cords: ClassicCoordinatesModel, value: Cell)
    operator fun get(cords: ClassicCoordinatesModel): Cell
    fun isFull(): Boolean
}
