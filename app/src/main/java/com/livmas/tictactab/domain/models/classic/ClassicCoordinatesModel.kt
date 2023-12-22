package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.ICoordinatesModel

data class ClassicCoordinatesModel(
    override val x: Int,
    override val y: Int
) : ICoordinatesModel
