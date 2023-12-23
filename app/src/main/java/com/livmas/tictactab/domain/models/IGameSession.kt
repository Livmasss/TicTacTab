package com.livmas.tictactab.domain.models

import com.livmas.tictactab.ui.GameMessage

interface IGameSession {
    fun makeTurn(cords: ICoordinatesModel): GameMessage
}