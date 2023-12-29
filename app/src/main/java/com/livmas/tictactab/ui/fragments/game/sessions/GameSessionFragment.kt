package com.livmas.tictactab.ui.fragments.game.sessions

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.livmas.tictactab.R
import com.livmas.tictactab.domain.models.CellModel
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player

abstract class GameSessionFragment: Fragment() {
    open val viewModel: GameSessionViewModel by activityViewModels()
    protected lateinit var fieldContainer: FrameLayout

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
    protected abstract fun renderCell(imageButton: ImageButton, cords: ICoordinatesModel)
    protected fun renderField(field: IFieldModel, idsField: Array<Array<ImageButton>> ) {
        for (x in 0..2)
            for (y in 0..2)
                renderCell(idsField[x][y], field[ClassicCoordinatesModel(x, y)])
    }
    protected fun renderCell(imageView: ImageView, cell: CellModel) = imageView.setImageDrawable(
        when(cell.state) {
            CellState.X -> xDrawable
            CellState.O -> oDrawable
            CellState.N -> null
            null -> null
        }
    )

    protected fun showLine(offset: Float = 0f, angle: Float = 0f) {
        val view = layoutInflater.inflate(R.layout.final_line_layout, fieldContainer, false) as ConstraintLayout

        addConstLayout(view, offset, angle)
    }

    private fun addConstLayout(view: ConstraintLayout, offset: Float = 0f, angle: Float = 0f) {
        view.rotation = angle

        ConstraintSet().apply {
            clone(context, R.layout.final_line_layout)
            setVerticalBias(R.id.vLine, 0.5f + offset)
            applyTo(view)
        }

        fieldContainer.addView(view, 1)
    }

    protected fun definePlayerDrawable(player: Player): Drawable? {
        return if (player== Player.X) xDrawable else oDrawable
    }
}