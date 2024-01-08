package com.livmas.tictactab.domain.game_sessions.complex_sessions

import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexFieldModel
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import java.util.Stack

class StackComplexSession(
    field: ComplexFieldModel,
    current: Player?,
    result: GameResult?
): BasicComplexSession(field, current, result) {
    constructor() : this(ComplexFieldModel(), Player.X, null)

    private val backStack = Stack<ClassicCoordinatesModel>()
}