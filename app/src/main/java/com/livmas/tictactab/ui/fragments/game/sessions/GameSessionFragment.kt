package com.livmas.tictactab.ui.fragments.game.sessions

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.livmas.tictactab.R

abstract class GameSessionFragment: Fragment() {
    open val viewModel: GameSessionViewModel by activityViewModels()

    protected var xDrawable: Drawable? = null
    protected var oDrawable: Drawable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initiates drawables for cell states
        xDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_x_cell, null)
        xDrawable?.setTint(ResourcesCompat.getColor(resources, R.color.first_player, null))
        oDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_o_cell, null)
        oDrawable?.setTint(ResourcesCompat.getColor(resources, R.color.second_player, null))

    }
}