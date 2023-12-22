package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel


interface IFieldModel<VT> {
    fun set(cords: ClassicCoordinatesModel, value: VT)
    operator fun get(cords: ClassicCoordinatesModel): VT
    fun isFull(): Boolean
}
