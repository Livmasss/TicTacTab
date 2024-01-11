package com.livmas.tictactab.ui.fragments.game.sessions.complex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.livmas.tictactab.GAME_TAG
import com.livmas.tictactab.R
import com.livmas.tictactab.databinding.FragmentComplexGameSessionBinding
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexCell
import com.livmas.tictactab.domain.models.complex.ComplexCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexFieldModel
import com.livmas.tictactab.domain.models.enums.CellState
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

        fieldContainer = binding.flFieldContainer

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        viewModel.field.value?.let { renderWholeField(it as ComplexFieldModel) }
        initObservers()
        viewModel.resumeGame()
    }

    override fun renderCell(imageButton: ImageButton, cords: ICoordinatesModel) {
        viewModel.field.value?.get(ClassicCoordinatesModel(cords.x, cords.y))?.let {
            val cell = (it as ComplexCell).field[
                    (cords as ComplexCoordinatesModel).innerCoordinates
            ]
            renderCell(imageButton, cell)
        }
    }

    private fun initViews() {
        initBlocksListeners()

        binding.bRestart.setOnClickListener {
            try {
                binding.flFieldContainer.removeViewAt(1)
            }
            catch (e: NullPointerException) {
                Log.d(GAME_TAG, "No line drawn")
            }
            viewModel.restartGame()
        }
    }
    private fun initBlocksListeners() {
        for (i in 0..8) {
            val x = i / 3
            val y = i % 3

            val id = resources.getIdentifier("block$x$y", "id", context?.packageName)
            val block = binding.root.findViewById(id) as ConstraintLayout

            initBlockListeners(block, ClassicCoordinatesModel(x, y))
        }
    }
    private fun initBlockListeners(block: ConstraintLayout, cords: ClassicCoordinatesModel) {
        for (i in 0..8) {
            val x = i / 3
            val y = i % 3

            val id = resources.getIdentifier("ibCell$x$y", "id", context?.packageName)
            val cell = block.findViewById(id) as View

            cell.setOnClickListener(makeTurnListener(ComplexCoordinatesModel(cords.x, cords.y, ClassicCoordinatesModel(x, y))))
        }
    }
    private fun initObservers() {
        viewLifecycleOwner.also { owner ->
            viewModel.apply {
                lastTurn.observe(owner) {
                    if (it == null)
                        viewModel.field.value?.let { field -> renderAll(field as ComplexFieldModel) }
                    it?.let {
                        val button = getImageButton(it as ComplexCoordinatesModel)
                        renderCell(button, it)
                        field.value?.let { field ->
                            renderBlockState(field as ComplexFieldModel, ClassicCoordinatesModel(it.x, it.y))
                        }
                    }
                }
                currentPlayer.observe(owner) {
                    binding.ivGameDisplay.apply {
                        setImageDrawable(definePlayerDrawable(it))
                        contentDescription = resources.getString(R.string.iv_display_desc, it)
                    }
                }
                gameResult.observe(owner) {
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
                alert.observe(owner) {
                    it?.let { messageCode ->
                        Snackbar.make(
                            binding.root,
                            when(messageCode) {
                                Alert.SomeError -> resources.getString(R.string.internal_error_message)
                                Alert.CellOccupied -> resources.getString(R.string.entity_occupied_message, resources.getString(R.string.cell_var))
                                Alert.GameFinished -> resources.getString(R.string.game_finished_message)
                                Alert.BlockFinished -> resources.getString(R.string.entity_occupied_message, resources.getString(R.string.block_var))
                                Alert.BlockInactive -> resources.getString(R.string.block_inactive_message)
                            },
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    viewModel.clearAlert()
                }
                winLineCode.observe(owner) {
                    when (it) {
                        0 -> return@observe
                        1 -> showLine(offset = -0.35f)
                        2 -> showLine()
                        3 -> showLine(offset = 0.35f)
                        4 -> showLine(angle = 45f)
                        5 -> showLine(angle = -45f)
                        6 -> showLine(offset = 0.35f, angle = 90f)
                        7 -> showLine(angle = 90f)
                        8 -> showLine(offset = -0.35f, angle = 90f)
                    }
                }
            }
        }
    }

    private fun renderAll(field: ComplexFieldModel) {
        renderWholeField(field)
        renderBlockStates(field)
    }

    private fun renderWholeField(field: ComplexFieldModel) {
        for (i in 0..8)
            renderField(field[ClassicCoordinatesModel(i / 3, i % 3)].field, blocks[i])
    }

    private fun renderBlockStates(field: ComplexFieldModel) {
        for (x in 0..2)
            for (y in 0..2) {
                renderBlockState(field, ClassicCoordinatesModel(x, y))
            }
    }

    private fun renderBlockState(field: ComplexFieldModel, cords: ClassicCoordinatesModel) {
        val container: ImageView by lazy {
            val id = resources.getIdentifier("image${cords.x}${cords.y}", "id", requireContext().packageName)
            binding.root.findViewById(id)
        }
        renderBlockState(container, field[cords].state)
    }

    private fun renderBlockState(container: ImageView, state: CellState?) {
        when (state) {
            null -> container.setImageDrawable(null)
            CellState.N -> container.setImageDrawable(null)
            CellState.X -> container.setImageDrawable(xDrawable)
            CellState.O -> container.setImageDrawable(oDrawable)
        }
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
    private fun makeTurnListener(cords: ComplexCoordinatesModel): View.OnClickListener {
        return View.OnClickListener {
            viewModel.makeTurn(cords)
        }
    }

    private fun getImageButton(cords: ComplexCoordinatesModel) =
        blocks[cords.x*3 + cords.y][cords.innerCoordinates.x][cords.innerCoordinates.y]
}