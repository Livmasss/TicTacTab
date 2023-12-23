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
    var isRun = true

    fun startGame(session: ClassicGameSession, run: Boolean?) {
        game = session
        if (run != null) {
            isRun = run
        }
    }
    fun stopGame() {
        isRun = false
    }

    fun makeTurn(cords: ClassicCoordinatesModel): GameMessage {
        if (!isRun)
            return GameMessage("This game ended! You can restart it.", 31)
        game?.let { game ->
            val message = game.makeTurn(cords)
            return (message)
        }
        Log.d(GameSession.TAG, "Game object is null")
        return GameMessage(null, 30)
    }
}