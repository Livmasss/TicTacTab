package com.livmas.tictactab.domain.models

import com.livmas.tictactab.domain.models.enums.CellState

open class Cell(
    var state: CellState
    ) {
    constructor(): this(CellState.N)
}