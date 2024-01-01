package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.CellModel
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState

data class ComplexFieldModel(
    private val data: Array<Array<ComplexCell>> = arrayOf(
        arrayOf(ComplexCell(), ComplexCell(), ComplexCell()),
        arrayOf(ComplexCell(), ComplexCell(), ComplexCell()),
        arrayOf(ComplexCell(), ComplexCell(), ComplexCell())
    )
): IFieldModel {
    fun set(cords: ClassicCoordinatesModel, value: ComplexCell) {
        data[cords.x][cords.y] = value
    }
    override fun set(cords: ClassicCoordinatesModel, value: CellModel) {
        data[cords.x][cords.y] = value as ComplexCell
    }

    override fun set(cords: ICoordinatesModel, value: CellState) {
        data[cords.x][cords.y].field.setState((cords as ComplexCoordinatesModel).innerCoordinates, value)
    }

    override fun get(cords: ClassicCoordinatesModel): ComplexCell {
        return data[cords.x][cords.y]
    }

    override fun get(cords: ICoordinatesModel): CellState {
        cords as ComplexCoordinatesModel
        return this[ClassicCoordinatesModel(cords)].field[cords.innerCoordinates].state ?: CellState.N
    }

    override fun isFull(): Boolean = data.all { row ->
            row.all { block ->
                block.field.isFull()
            }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComplexFieldModel

        if (!data.contentDeepEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentDeepHashCode()
    }
}