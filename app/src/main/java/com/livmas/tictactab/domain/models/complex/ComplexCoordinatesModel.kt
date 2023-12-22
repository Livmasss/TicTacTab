package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel

data class ComplexCoordinatesModel(
    override val x: Int,
    override val y: Int,

    val innerCoordinates: ClassicCoordinatesModel
): ICoordinatesModel