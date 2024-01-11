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
        blocks = Array(9) {
            Array(3) { _ ->
                Array(3) { _ ->
                    ImageButton(context)
                }
            }
        }
        getImageButtons()

        fieldContainer = binding.flFieldContainer
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        viewModel.field.value?.let { renderAll(it as ComplexFieldModel) }
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

            val block = findBlock(ClassicCoordinatesModel(x, y))
            initBlockListeners(block, ClassicCoordinatesModel(x, y))
        }
    }
    private fun findBlock(cords: ClassicCoordinatesModel): ConstraintLayout {
        val id = getIdByString("block${cords.x}${cords.y}")
        return binding.root.findViewById(id) as ConstraintLayout
    }
    private fun initBlockListeners(block: ConstraintLayout, cords: ClassicCoordinatesModel) {
        for (i in 0..8) {
            val x = i / 3
            val y = i % 3

            val id = getIdByString("ibCell$x$y")
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
            val id = getIdByString("image${cords.x}${cords.y}")
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

    private fun findCell(block: ConstraintLayout, cords: ClassicCoordinatesModel): ImageButton {
        return getIdByString("ibCell${cords.x}${cords.y}").let {
            block.findViewById(it)
        }
    }

    private fun getImageButtons() {
        for (i in 0..8) {
            val x = i / 3
            val y = i % 3

            val block = findBlock(ClassicCoordinatesModel(x, y))

            for (j in 0..8) {
                val x1 = j / 3
                val y1 = j % 3

                blocks[i][x1][y1] = findCell(block, ClassicCoordinatesModel(x1, y1))
            }
        }
    }
    private fun makeTurnListener(cords: ComplexCoordinatesModel): View.OnClickListener {
        return View.OnClickListener {
            viewModel.makeTurn(cords)
        }
    }

    private fun getIdByString(name: String): Int {
        return resources.getIdentifier(name, "id", context?.packageName)
    }

    private fun getImageButton(cords: ComplexCoordinatesModel) =
        blocks[cords.x*3 + cords.y][cords.innerCoordinates.x][cords.innerCoordinates.y]
}