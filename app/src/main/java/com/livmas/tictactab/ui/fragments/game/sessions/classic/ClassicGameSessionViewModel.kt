package com.livmas.tictactab.ui.fragments.game.sessions.classic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.livmas.tictactab.domain.models.ClassicGameManager
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.enums.Player

class ClassicGameSessionViewModel : ViewModel() {
    val field: MutableLiveData<ClassicFieldModel> by lazy {
        MutableLiveData<ClassicFieldModel>(ClassicFieldModel())
    }
    val currentPlayer: MutableLiveData<Player> by lazy {
        MutableLiveData<Player>(Player.X)
    }
    val winner: MutableLiveData<Player?> by lazy {
        MutableLiveData<Player?>(null)
    }

    private val gameManager = ClassicGameManager(ClassicFieldModel())

    fun startGame() {
        gameManager.startGame()
        field.value = gameManager.field
    }
    fun stopGame() {
        gameManager.stopGame()
    }
    fun makeTurn(cords: ClassicCoordinatesModel) {
        gameManager.makeTurn(cords)
        field.value = gameManager.field
    }
}