package com.livmas.tictactab.ui.fragments.game.sessions.classic

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.livmas.tictactab.R
import com.livmas.tictactab.databinding.FragmentClassicGameSessionBinding
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicFieldModel
import com.livmas.tictactab.domain.models.classic.ClassicGameSession
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.models.enums.Alert

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
        binding = FragmentClassicGameSessionBinding.inflate(LayoutInflater.from(context), container, false)

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
            try {
                binding.flFieldContainer.removeViewAt(1)
            }
            catch (e: NullPointerException) {
                Log.d(ClassicGameSession.TAG, "No line drawn")
            }
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
            currentPlayer.observe(viewLifecycleOwner) {
                binding.ivGameDisplay.apply {
                    setImageDrawable(definePlayerDrawable(it))
                    contentDescription = resources.getString(R.string.iv_display_desc, it)
                }
            }
            gameResult.observe(viewLifecycleOwner) {
                when (it) {
                    null -> binding.apply {
                        tvGameDisplay.text = resources.getString(R.string.current_player_message)
                        ivGameDisplay.setImageDrawable(
                            if (currentPlayer.value == null)
                                xDrawable else definePlayerDrawable(
                                currentPlayer.value!!
                            )
                        )
                        ivGameDisplay.contentDescription =
                            resources.getString(R.string.iv_display_desc, it)
                    }
                    GameResult.N ->binding.apply {
                        tvGameDisplay.text = resources.getString(R.string.draw_message)
                        ivGameDisplay.setImageDrawable(null)
                        ivGameDisplay.contentDescription = resources.getString(R.string.draw_message)
                    }
                    else -> binding.apply {
                        tvGameDisplay.text = resources.getString(R.string.winner_message)
                        ivGameDisplay.setImageDrawable(if (it == GameResult.X) xDrawable else oDrawable)
                        ivGameDisplay.contentDescription = resources.getString(R.string.iv_display_desc, it)
                    }
                }
            }
            alert.observe(viewLifecycleOwner) {
                it?.let { messageCode ->
                    Snackbar.make(
                        binding.root,
                        when(messageCode) {
                            Alert.SomeError -> resources.getString(R.string.internal_error_message)
                            Alert.CellOccupied -> resources.getString(R.string.cell_occupied_message)
                            Alert.GameFinished -> resources.getString(R.string.game_finished_message)
                                          },
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                viewModel.clearAlert()
            }
            winLineCode.observe(viewLifecycleOwner) {
                when (it) {
                    0 -> return@observe
                    1 -> showLine(offset = -1f/3f)
                    2 -> showLine()
                    3 -> showLine(offset = 1f/3f)
                    4 -> showLine(angle = 45f)
                    5 -> showLine(angle = -45f)
                    6 -> showLine(offset = 1f/3f, angle = 90f)
                    7 -> showLine(angle = 90f)
                    8 -> showLine(offset = -1f/3f, angle = 90f)
                }
            }
        }
    }
    private fun definePlayerDrawable(player: Player): Drawable? {
        return if (player==Player.X) xDrawable else oDrawable
    }
    private fun showLine(offset: Float = 0f, angle: Float = 0f) {
        val view = layoutInflater.inflate(R.layout.final_line_layout, binding.flFieldContainer, false) as ConstraintLayout
        view.rotation = angle

        ConstraintSet().apply {
            clone(context, R.layout.final_line_layout)
            setVerticalBias(R.id.vLine, 0.5f + offset)
            applyTo(view)
        }

        binding.flFieldContainer.addView(view, 1)
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