package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.CellValue
import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.enums.CellState

data class ClassicFieldModel(
    private val data: Array<Array<CellState>> = arrayOf(
        arrayOf(CellState.N, CellState.N, CellState.N),
        arrayOf(CellState.N, CellState.N, CellState.N),
        arrayOf(CellState.N, CellState.N, CellState.N)
    )
) : IFieldModel {
    fun set(cords: ClassicCoordinatesModel, value: CellState) {
        data[cords.x][cords.y] = value
    }
    override fun set(cords: ClassicCoordinatesModel, value: CellValue) {
        this.set(cords, value as CellState)
    }
    override operator fun get(cords: ClassicCoordinatesModel): CellState {
        return data[cords.x][cords.y]
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
