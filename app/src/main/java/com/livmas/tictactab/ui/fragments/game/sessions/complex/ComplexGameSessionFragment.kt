package com.livmas.tictactab.ui.fragments.game.sessions.complex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.livmas.tictactab.R
import com.livmas.tictactab.databinding.ClassicGameFieldLayoutBinding
import com.livmas.tictactab.databinding.FragmentComplexGameSessionBinding
import com.livmas.tictactab.domain.models.GameSession
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexFieldModel
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.ui.fragments.game.sessions.GameSessionFragment
import com.livmas.tictactab.ui.models.enums.Alert

class ComplexGameSessionFragment : GameSessionFragment() {

    private lateinit var blocks: Array<Array<Array<ImageButton>>>
    override val viewModel: ComplexGameSessionViewModel by activityViewModels()
    private lateinit var binding: FragmentComplexGameSessionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComplexGameSessionBinding.inflate(inflater, container, false)
        blocks = Array(9) { arrayOf(arrayOf(ImageButton(context))) }
        getImageButtons()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        viewModel.field.value?.let { renderAll(it) }
        initObservers()
        viewModel.resumeGame()
    }

    private fun initViews() {
        initCells()

        binding.bRestart.setOnClickListener {
            try {
                binding.flFieldContainer.removeViewAt(1)
            }
            catch (e: NullPointerException) {
                Log.d(GameSession.TAG, "No line drawn")
            }
            viewModel.restartGame()
        }
    }
    private fun initCells() {
        initBlock(binding.block00, ClassicCoordinatesModel(0, 0))
        initBlock(binding.block01, ClassicCoordinatesModel(0, 1))
        initBlock(binding.block02, ClassicCoordinatesModel(0, 2))
        initBlock(binding.block10, ClassicCoordinatesModel(1, 0))
        initBlock(binding.block11, ClassicCoordinatesModel(1, 1))
        initBlock(binding.block12, ClassicCoordinatesModel(1, 2))
        initBlock(binding.block20, ClassicCoordinatesModel(2, 0))
        initBlock(binding.block21, ClassicCoordinatesModel(2, 1))
        initBlock(binding.block22, ClassicCoordinatesModel(2, 2))
    }
    private fun initBlock(block: ClassicGameFieldLayoutBinding, cords: ClassicCoordinatesModel) {
        block.apply {
            ibCell00.setOnClickListener(makeTurnListener(ComplexCoordinatesModel(cords.x, cords.y, ClassicCoordinatesModel(0, 0))))
            ibCell01.setOnClickListener(makeTurnListener(ComplexCoordinatesModel(cords.x, cords.y, ClassicCoordinatesModel(0, 1))))
            ibCell02.setOnClickListener(makeTurnListener(ComplexCoordinatesModel(cords.x, cords.y, ClassicCoordinatesModel(0, 2))))
            ibCell10.setOnClickListener(makeTurnListener(ComplexCoordinatesModel(cords.x, cords.y, ClassicCoordinatesModel(1, 0))))
            ibCell11.setOnClickListener(makeTurnListener(ComplexCoordinatesModel(cords.x, cords.y, ClassicCoordinatesModel(1, 1))))
            ibCell12.setOnClickListener(makeTurnListener(ComplexCoordinatesModel(cords.x, cords.y, ClassicCoordinatesModel(1, 2))))
            ibCell20.setOnClickListener(makeTurnListener(ComplexCoordinatesModel(cords.x, cords.y, ClassicCoordinatesModel(2, 0))))
            ibCell21.setOnClickListener(makeTurnListener(ComplexCoordinatesModel(cords.x, cords.y, ClassicCoordinatesModel(2, 1))))
            ibCell22.setOnClickListener(makeTurnListener(ComplexCoordinatesModel(cords.x, cords.y, ClassicCoordinatesModel(2, 2))))
        }
    }
    private fun initObservers() {
        viewModel.apply {
            field.observe(viewLifecycleOwner) {
                renderAll(it)
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
                            resources.getString(R.string.iv_display_desc, null)
                    }
                    GameResult.N -> binding.apply {
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
                            Alert.CellOccupied -> resources.getString(R.string.entity_occupied_message, resources.getString(R.string.cell_var))
                            Alert.GameFinished -> resources.getString(R.string.game_finished_message)
                            Alert.BlockFinished -> resources.getString(R.string.entity_occupied_message, resources.getString(R.string.block_var))
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

    private fun renderAll(field: ComplexFieldModel) {
        renderField(field[ClassicCoordinatesModel(0, 0)].field, blocks[0])
        renderField(field[ClassicCoordinatesModel(0, 1)].field, blocks[1])
        renderField(field[ClassicCoordinatesModel(0, 2)].field, blocks[2])
        renderField(field[ClassicCoordinatesModel(1, 0)].field, blocks[3])
        renderField(field[ClassicCoordinatesModel(1, 1)].field, blocks[4])
        renderField(field[ClassicCoordinatesModel(1, 2)].field, blocks[5])
        renderField(field[ClassicCoordinatesModel(2, 0)].field, blocks[6])
        renderField(field[ClassicCoordinatesModel(2, 1)].field, blocks[7])
        renderField(field[ClassicCoordinatesModel(2, 2)].field, blocks[8])
    }

    private fun getImageButtons() {
        binding.block00.apply {
            blocks[0] = arrayOf(
                arrayOf(ibCell00, ibCell01, ibCell02),
                arrayOf(ibCell10, ibCell11, ibCell12),
                arrayOf(ibCell20, ibCell21, ibCell22)
            )
        }
        binding.block01.apply {
            blocks[1] = arrayOf(
                arrayOf(ibCell00, ibCell01, ibCell02),
                arrayOf(ibCell10, ibCell11, ibCell12),
                arrayOf(ibCell20, ibCell21, ibCell22)
            )
        }
        binding.block02.apply {
            blocks[2] = arrayOf(
                arrayOf(ibCell00, ibCell01, ibCell02),
                arrayOf(ibCell10, ibCell11, ibCell12),
                arrayOf(ibCell20, ibCell21, ibCell22)
            )
        }
        binding.block10.apply {
            blocks[3] = arrayOf(
                arrayOf(ibCell00, ibCell01, ibCell02),
                arrayOf(ibCell10, ibCell11, ibCell12),
                arrayOf(ibCell20, ibCell21, ibCell22)
            )
        }
        binding.block11.apply {
            blocks[4] = arrayOf(
                arrayOf(ibCell00, ibCell01, ibCell02),
                arrayOf(ibCell10, ibCell11, ibCell12),
                arrayOf(ibCell20, ibCell21, ibCell22)
            )
        }
        binding.block12.apply {
            blocks[5] = arrayOf(
                arrayOf(ibCell00, ibCell01, ibCell02),
                arrayOf(ibCell10, ibCell11, ibCell12),
                arrayOf(ibCell20, ibCell21, ibCell22)
            )
        }
        binding.block20.apply {
            blocks[6] = arrayOf(
                arrayOf(ibCell00, ibCell01, ibCell02),
                arrayOf(ibCell10, ibCell11, ibCell12),
                arrayOf(ibCell20, ibCell21, ibCell22)
            )
        }
        binding.block21.apply {
            blocks[7] = arrayOf(
                arrayOf(ibCell00, ibCell01, ibCell02),
                arrayOf(ibCell10, ibCell11, ibCell12),
                arrayOf(ibCell20, ibCell21, ibCell22)
            )
        }
        binding.block22.apply {
            blocks[8] = arrayOf(
                arrayOf(ibCell00, ibCell01, ibCell02),
                arrayOf(ibCell10, ibCell11, ibCell12),
                arrayOf(ibCell20, ibCell21, ibCell22)
            )
        }
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
    private fun makeTurnListener(cords: ComplexCoordinatesModel): View.OnClickListener {
        return View.OnClickListener {
            viewModel.makeTurn(cords)
        }
    }
}