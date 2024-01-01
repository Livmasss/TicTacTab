package com.livmas.tictactab.domain.models.classic

import com.livmas.tictactab.domain.models.CellModel
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.enums.CellState

data class ClassicFieldModel(
    private val data: Array<Array<CellModel>> = arrayOf(
        arrayOf(CellModel(), CellModel(), CellModel()),
        arrayOf(CellModel(), CellModel(), CellModel()),
        arrayOf(CellModel(), CellModel(), CellModel())
    )
) : IFieldModel {
    override fun set(cords: ClassicCoordinatesModel, value: CellModel) {
        data[cords.x][cords.y] = value
    }

    override fun set(cords: ICoordinatesModel, value: CellState) {
        data[cords.x][cords.y].state = value
    }

    fun setState(cords: ClassicCoordinatesModel, value: CellState) {
        data[cords.x][cords.y].state = value
    }
    override operator fun get(cords: ClassicCoordinatesModel): CellModel {
        return data[cords.x][cords.y]
    }

    override fun get(cords: ICoordinatesModel): CellState {
        cords as ClassicCoordinatesModel
        return this[cords].state ?: CellState.N
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
