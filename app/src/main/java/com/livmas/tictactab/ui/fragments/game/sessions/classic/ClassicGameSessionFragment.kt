package com.livmas.tictactab.ui.fragments.game.sessions.classic

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.livmas.tictactab.R
import com.livmas.tictactab.databinding.FragmentClassicGameSessionBinding
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.enums.CellState

class ClassicGameSessionFragment : Fragment() {

    private val viewModel: ClassicGameSessionViewModel by activityViewModels()
    private lateinit var binding: FragmentClassicGameSessionBinding
    private lateinit var idsField: Array<Array<ImageButton>>

    private var xDrawable: Drawable? = null
    private var oDrawable: Drawable? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassicGameSessionBinding.inflate(inflater, container, false)

        binding.field.apply {
            idsField = arrayOf(
                arrayOf(ibCell00, ibCell01, ibCell02),
                arrayOf(ibCell10, ibCell11, ibCell12),
                arrayOf(ibCell20, ibCell21, ibCell22)
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initiates drawables for cell states
        xDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_x_cell, null)
        xDrawable?.setTint(ResourcesCompat.getColor(resources, R.color.first_player, null))
        oDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_o_cell, null)
        oDrawable?.setTint(ResourcesCompat.getColor(resources, R.color.second_player, null))

        initViews()
        viewModel.field.value?.let { renderField(it) }
        initObservers()
        viewModel.startGame()
    }

    private fun initViews() {
        initCells()

        binding.bRestart.setOnClickListener {
            viewModel.restartGame()
        }
    }
    private fun initCells() {
        binding.field.apply {
            ibCell00.setOnClickListener(makeTurnListener(ClassicCoordinatesModel(0, 0)))
            ibCell01.setOnClickListener(makeTurnListener(ClassicCoordinatesModel(0, 1)))
            ibCell02.setOnClickListener(makeTurnListener(ClassicCoordinatesModel(0, 2)))
            ibCell10.setOnClickListener(makeTurnListener(ClassicCoordinatesModel(1, 0)))
            ibCell11.setOnClickListener(makeTurnListener(ClassicCoordinatesModel(1, 1)))
            ibCell12.setOnClickListener(makeTurnListener(ClassicCoordinatesModel(1, 2)))
            ibCell20.setOnClickListener(makeTurnListener(ClassicCoordinatesModel(2, 0)))
            ibCell21.setOnClickListener(makeTurnListener(ClassicCoordinatesModel(2, 1)))
            ibCell22.setOnClickListener(makeTurnListener(ClassicCoordinatesModel(2, 2)))
        }
    }
    private fun initObservers() {
        viewModel.apply {
            field.observe(viewLifecycleOwner) {
                renderField(it)
            }
            winner.observe(viewLifecycleOwner) {
                if (it == null)
                    return@observe
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.winning_message, it.toString()),
                    Snackbar.LENGTH_LONG
                ).show()
            }
            gameFinished.observe(viewLifecycleOwner) {
                if (it && winner.value == null)
                    Snackbar.make(
                        binding.root,
                        "Draw! You can try again",
                        Snackbar.LENGTH_LONG
                    ).show()
            }
            alert.observe(viewLifecycleOwner) {
                it?.let { message ->
                    Snackbar.make(
                        binding.root,
                        message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    private fun makeTurnListener(cords: ClassicCoordinatesModel): OnClickListener {
        return OnClickListener {
            viewModel.makeTurn(cords)
        }
    }
    private fun renderField(field: ClassicFieldModel) {
        for (x in 0..2)
            for (y in 0..2) {
                when (field[x, y]) {
                    CellState.X -> idsField[x][y].setImageDrawable(xDrawable)
                    CellState.O -> idsField[x][y].setImageDrawable(oDrawable)
                    CellState.N -> idsField[x][y].setImageDrawable(null)
                }
            }
    }
}