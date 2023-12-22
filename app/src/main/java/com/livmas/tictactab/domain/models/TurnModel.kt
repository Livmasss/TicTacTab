package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.enums.Player

data class TurnModel(
    val cords: ICoordinatesModel,
    val player: Player
)
