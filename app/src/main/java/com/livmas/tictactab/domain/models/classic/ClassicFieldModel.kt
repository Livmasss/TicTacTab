package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.Cell
import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.complex.ComplexCell
import com.livmas.tictactab.domain.models.enums.CellState

data class ClassicFieldModel(
    private val data: Array<Array<Cell>> = arrayOf(
        arrayOf(Cell(), Cell(), Cell()),
        arrayOf(Cell(), Cell(), Cell()),
        arrayOf(Cell(), Cell(), Cell())
    )
) : IFieldModel {
    override fun set(cords: ClassicCoordinatesModel, value: Cell) {
        data[cords.x][cords.y] = value
    }
    fun setState(cords: ClassicCoordinatesModel, value: CellState) {
        data[cords.x][cords.y].state = value
    }
    override operator fun get(cords: ClassicCoordinatesModel): Cell {
        return data[cords.x][cords.y]
    }

    override fun isFull(): Boolean = !data.any { row ->
        row.any { cell ->
            cell.state==CellState.N
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
