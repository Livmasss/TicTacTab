package com.livmas.tictactab.domain.models

import com.livmas.tictactab.ui.GameMessage

interface IGameSession<FT, CT> {
    val field: FT

    fun makeTurn(cords: CT): GameMessage
}