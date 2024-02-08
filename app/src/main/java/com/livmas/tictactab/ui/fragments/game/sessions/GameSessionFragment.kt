package com.livmas.tictactab.ui.fragments.game.sessions

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.livmas.tictactab.R
import com.livmas.tictactab.UI_TAG
import com.livmas.tictactab.domain.models.CellModel
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.IFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.Player


abstract class GameSessionFragment: Fragment() {
    open val viewModel: GameSessionViewModel by activityViewModels()
    protected lateinit var fieldContainer: FrameLayout //Layout containing game field

    // Drawables for X and O icons
    protected var xDrawable: Drawable? = null
    protected var oDrawable: Drawable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        xDrawable = prepareCellStateDrawable(
            R.drawable.ic_x_cell,
            com.google.android.material.R.attr.colorSecondary,
            R.color.orange_200
        )

        oDrawable = prepareCellStateDrawable(
            R.drawable.ic_o_cell,
            com.google.android.material.R.attr.colorPrimary,
            R.color.purple_200
        )
    }

    //Initiates drawables for cell states
    private fun prepareCellStateDrawable(drawableId: Int, colorAttrId: Int, altColorId: Int): Drawable? {
        val drawable = ResourcesCompat.getDrawable(resources, drawableId, null)
        drawable?.setTint(
            context?.let {
                val typedValue = TypedValue()
                it.theme.resolveAttribute(colorAttrId, typedValue, true)
                typedValue.data
            } ?: ResourcesCompat.getColor(resources, altColorId, null)
        )
        return drawable
    }

    // Update cell according to its value in field
    protected abstract fun renderCell(imageButton: ImageButton, cords: ICoordinatesModel)

    // Update drawable of ImageView
    protected fun renderCell(imageView: ImageView, cell: CellModel) = imageView.setImageDrawable(
        when(cell.state) {
            CellState.X -> xDrawable
            CellState.O -> oDrawable
            CellState.N -> null
            null -> null
        }
    )

    protected fun renderField(field: IFieldModel, idsField: Array<Array<ImageButton>> ) {
        for (x in 0..2)
            for (y in 0..2)
                renderCell(idsField[x][y], field[ClassicCoordinatesModel(x, y)])
    }

    //Inflates win line
    protected fun rerenderLine(offset: Float = 0f, angle: Float = 0f) {
        removeLine()

        val view = layoutInflater.inflate(R.layout.final_line_layout, fieldContainer, false) as ConstraintLayout

        fieldContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                fieldContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)

                view.layoutParams.width = if ((angle.toInt() / 45) % 2 == 0)
                    (fieldContainer.width * 0.99).toInt()
                else
                    (fieldContainer.width * 1.35).toInt()

                view.requestLayout()
            }
        })

        addConstLayout(view, offset, angle)
    }

    protected fun removeLine() {
        try {
            fieldContainer.removeViewAt(1)
        }
        catch (e: NullPointerException) {
            Log.d(UI_TAG, "No line drawn")
        }
    }

    //Adds ConstraintLayout to fieldContainer
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