package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player

data class ClassicFieldModel(
    private val data: Array<Array<CellState>> = arrayOf(
        arrayOf(CellState.N, CellState.N, CellState.N),
        arrayOf(CellState.N, CellState.N, CellState.N),
        arrayOf(CellState.N, CellState.N, CellState.N)
    )
) {
    fun set(cords: ClassicCoordinatesModel, value: CellState) {
        data[cords.x][cords.y] = value
    }
    fun get(cords: ClassicCoordinatesModel): CellState {
        return data[cords.x][cords.y]
    }

    fun isFinished() {
        
    }

    fun makeTurn(turn: ClassicTurnModel) {
        val state = if (turn.player == Player.X)
            CellState.X
        else
            CellState.O
        set(turn.cords, state)
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
