package com.livmas.tictactab.domain.models.enums

import com.livmas.tictactab.domain.models.CellValue

enum class CellState(value: Int): CellValue {
    N(0),
    X(1),
    O(2)
}