package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.enums.CellState

data class ClassicField(
    private val data: Array<Array<CellState>> = arrayOf(
        arrayOf(CellState.N, CellState.N, CellState.N),
        arrayOf(CellState.N, CellState.N, CellState.N),
        arrayOf(CellState.N, CellState.N, CellState.N)
    )
) {
    fun set(cords: ClassicCoordinates, value: CellState) {
        data[cords.x][cords.y] = value
    }
    fun get(cords: ClassicCoordinates): CellState {
        return data[cords.x][cords.y]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClassicField

        if (!data.contentDeepEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentDeepHashCode()
    }
}
