package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.enums.Player

data class ClassicTurnModel(
    val cords: ClassicCoordinatesModel,
    val player: Player
)
