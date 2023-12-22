package com.livmas.tictactab.domain.models.complex

import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicGameSession

data class ComplexFieldModel(
    private val data: Array<Array<ClassicGameSession>> = arrayOf(
        arrayOf(ClassicGameSession(), ClassicGameSession(), ClassicGameSession()),
        arrayOf(ClassicGameSession(), ClassicGameSession(), ClassicGameSession()),
        arrayOf(ClassicGameSession(), ClassicGameSession(), ClassicGameSession())
    )
): IFieldModel<ClassicGameSession> {
    override fun set(cords: ClassicCoordinatesModel, value: ClassicGameSession) {
        data[cords.x][cords.y] = value
    }

    override fun get(cords: ClassicCoordinatesModel): ClassicGameSession {
        return data[cords.x][cords.y]
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