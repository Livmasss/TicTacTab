package com.livmas.tictactab.domain.game_sessions

import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.ui.GameMessage

interface IGameSession {
    fun makeTurn(cords: ICoordinatesModel): GameMessage
}