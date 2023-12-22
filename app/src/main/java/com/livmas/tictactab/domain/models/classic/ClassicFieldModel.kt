package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.domain.models.exceptions.CellOccupiedException

data class ClassicFieldModel(
    private val data: Array<Array<CellState>> = arrayOf(
        arrayOf(CellState.N, CellState.N, CellState.N),
        arrayOf(CellState.N, CellState.N, CellState.N),
        arrayOf(CellState.N, CellState.N, CellState.N)
    )
) : IFieldModel<CellState> {
    override fun set(cords: ClassicCoordinatesModel, value: CellState) {
        data[cords.x][cords.y] = value
    }
    private operator fun get(cords: ClassicCoordinatesModel): CellState {
        return data[cords.x][cords.y]
    }
    override operator fun get(x: Int, y: Int): CellState {
        return data[x][y]
    }

    fun makeTurn(turn: ClassicTurnModel) {
        val state = if (turn.player == Player.X)
            CellState.X
        else
            CellState.O

        if (get(turn.cords) == CellState.N)
            set(turn.cords, state)
        else
            throw CellOccupiedException()
    }

    override fun isFull(): Boolean = !data.any { row ->
        row.any { cell ->
            cell==CellState.N
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClassicFieldModel

        if (!data.contentDeepEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentDeepHashCode()
    }
}
