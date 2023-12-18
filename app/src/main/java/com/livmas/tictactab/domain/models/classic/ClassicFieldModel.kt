package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.domain.models.exceptions.CellOccupiedException

data class ClassicFieldModel(
    private val _data: Array<Array<CellState>> = arrayOf(
        arrayOf(CellState.N, CellState.N, CellState.N),
        arrayOf(CellState.N, CellState.N, CellState.N),
        arrayOf(CellState.N, CellState.N, CellState.N)
    )
) {
    fun set(cords: ClassicCoordinatesModel, value: CellState) {
        _data[cords.x][cords.y] = value
    }
    operator fun get(cords: ClassicCoordinatesModel): CellState {
        return _data[cords.x][cords.y]
    }
    operator fun get(x: Int, y: Int): CellState {
        return _data[x][y]
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClassicFieldModel

        if (!_data.contentDeepEquals(other._data)) return false

        return true
    }

    override fun hashCode(): Int {
        return _data.contentDeepHashCode()
    }

    fun isFull(): Boolean = !_data.any { row ->
        row.any { cell ->
            cell==CellState.N
        }
    }
}
