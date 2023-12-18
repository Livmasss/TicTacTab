package com.livmas.tictactab.domain.models

import android.util.Log
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicGameSession
import com.livmas.tictactab.ui.GameMessage

class ClassicGameManager {
    private var game: ClassicGameSession? = null
    val field
        get() = if (game?.field == null) ClassicFieldModel() else game!!.field
    val winner
        get() = game?.winner
    val currentPlayer
        get() = game?.currentPlayer
    var isRun = false

    fun startGame(session: ClassicGameSession) {
        game = session
        isRun = true
    }
    fun stopGame() {
        isRun = false
    }

    //Returns true if game finished after this turn
    fun makeTurn(cords: ClassicCoordinatesModel): GameMessage {
        if (!isRun)
            return GameMessage("This game ended! You can restart it.", 31)
        game?.let { game ->
            val message = game.makeTurn(cords)
            return (message)
        }
        Log.d(ClassicGameSession.TAG, "Game object is null")
        return GameMessage(null, 30)
    }
}