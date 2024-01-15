package com.livmas.tictactab.domain.game_sessions.complex_sessions

import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexFieldModel
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import java.util.Stack

class StackComplexSession(
    field: ComplexFieldModel,
    current: Player?,
    result: GameResult?
): ChangeableComplexSession(field, current, result) {

    private val backStack = Stack<ClassicCoordinatesModel>()

        override fun postTurnProcess(cords: ICoordinatesModel) {
            backStack.push(ClassicCoordinatesModel(cords))
            super.postTurnProcess(cords)
        }

        override fun onChooseClosedBlock(): ClassicCoordinatesModel? {
            while (backStack.size > 0) {
                val cords = backStack.pop()

                if (field[cords].state == null)
                    return cords
            }
            return null
        }
    }
