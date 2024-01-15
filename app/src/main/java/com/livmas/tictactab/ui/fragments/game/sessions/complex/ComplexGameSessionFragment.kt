package com.livmas.tictactab.ui.fragments.game.sessions.complex

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.livmas.tictactab.R
import com.livmas.tictactab.UI_TAG
import com.livmas.tictactab.databinding.FragmentComplexGameSessionBinding
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexCell
import com.livmas.tictactab.domain.models.complex.ComplexCoordinatesModel
import com.livmas.tictactab.domain.models.complex.ComplexFieldModel
import com.livmas.tictactab.domain.models.enums.CellState
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.domain.models.enums.Player
import com.livmas.tictactab.ui.fragments.game.sessions.GameSessionFragment
import com.livmas.tictactab.ui.models.enums.Alert
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class ComplexGameSessionFragment : GameSessionFragment() {

    private lateinit var blockButtons: Array<Array<Array<ImageButton>>>
    override val viewModel: ComplexGameSessionViewModel by activityViewModels()
    private lateinit var binding: FragmentComplexGameSessionBinding
    private var prevBlockCords: ClassicCoordinatesModel? = null // Coordinates of previous active block

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initiating
        binding = FragmentComplexGameSessionBinding.inflate(inflater, container, false)

        blockButtons = Array(9) {
            Array(3) { _ ->
                Array(3) { _ ->
                    ImageButton(context)
                }
            }
        }
        initBlockButtons()

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

    // Update cell according to its ComplexCoordinates and value in field
    override fun renderCell(imageButton: ImageButton, cords: ICoordinatesModel) {
        viewModel.field.value?.get(ClassicCoordinatesModel(cords.x, cords.y))?.let {
            val cell = (it as ComplexCell).field[
                    (cords as ComplexCoordinatesModel).innerCoordinates
            ]
            renderCell(imageButton, cell)
        }
    }

    //Renders everything
    private fun renderAll(field: ComplexFieldModel) {
        renderWholeField(field)
        renderBlockStates(field)
    }

    //Renders whole field
    private fun renderWholeField(field: ComplexFieldModel) {
        for (i in 0..8)
            renderField(field[ClassicCoordinatesModel(i / 3, i % 3)].field, blockButtons[i])
    }

    //Renders every block states
    private fun renderBlockStates(field: ComplexFieldModel) {
        for (x in 0..2)
            for (y in 0..2) {
                renderBlockState(field, ClassicCoordinatesModel(x, y))
            }
    }

    //Renders block at coordinates state
    private fun renderBlockState(field: ComplexFieldModel, cords: ClassicCoordinatesModel) {
        val container: ImageView by lazy {
            val id = getIdByString("image${cords.x}${cords.y}")
            binding.root.findViewById(id)
        }
        renderBlockState(container, field[cords].state)
    }

    //Renders block state
    private fun renderBlockState(container: ImageView, state: CellState?) {
        when (state) {
            null -> container.setImageDrawable(null)
            CellState.N -> container.setImageDrawable(null)
            CellState.X -> container.setImageDrawable(xDrawable)
            CellState.O -> container.setImageDrawable(oDrawable)
        }
    }

    // Initiates all views appearance
    private fun initViews() {
        initBlocksListeners()

        binding.bRestart.setOnClickListener {
            if (viewModel.currentBlockCords.value != null) {
                val block = findBlock(viewModel.currentBlockCords.value!!)
                block.setBlockColor(androidx.appcompat.R.attr.colorPrimary)
            }

            try {
                binding.flFieldContainer.removeViewAt(1)
            }
            catch (e: NullPointerException) {
                Log.d(UI_TAG, "No line drawn")
            }
            viewModel.restartGame()
        }
    }
    //Initiates block listeners
    private fun initBlockListeners(block: ConstraintLayout, cords: ClassicCoordinatesModel) {
        for (i in 0..8) {
            val x = i / 3
            val y = i % 3

            val id = getIdByString("ibCell$x$y")
            val cell = block.findViewById(id) as View

            cell.setOnClickListener(makeTurnListener(ComplexCoordinatesModel(cords.x, cords.y, ClassicCoordinatesModel(x, y))))
        }
    }

    //Initiates listeners for every blocks
    private fun initBlocksListeners() {
        for (i in 0..8) {
            val x = i / 3
            val y = i % 3

            val block = findBlock(ClassicCoordinatesModel(x, y))
            initBlockListeners(block, ClassicCoordinatesModel(x, y))
        }
    }
    //Find block (ConstraintLayout) by coordinates
    private fun findBlock(cords: ClassicCoordinatesModel): ConstraintLayout {
        val id = getIdByString("block${cords.x}${cords.y}")
        return binding.root.findViewById(id) as ConstraintLayout
    }

    //Handles changes in view model. Used in observers
    private fun handleLastTurn(cords: ICoordinatesModel) {
        val button = getImageButton(cords as ComplexCoordinatesModel)
        renderCell(button, cords)
        viewModel.field.value?.let { field ->
            renderBlockState(field as ComplexFieldModel, ClassicCoordinatesModel(cords.x, cords.y))
        }
    }
    private fun handleResultN() {
        binding.apply {
            tvGameDisplay.text = resources.getString(R.string.draw_message)
            ivGameDisplay.setImageDrawable(null)
            ivGameDisplay.contentDescription = resources.getString(R.string.draw_message)
        }
    }

    private fun handleResultNull(currentPlayer: Player?) =
        binding.apply {
            tvGameDisplay.text = resources.getString(R.string.current_player_message)
            ivGameDisplay.setImageDrawable(
                if (currentPlayer == null)
                    xDrawable
                else
                    definePlayerDrawable(currentPlayer)
            )
            ivGameDisplay.contentDescription =
                resources.getString(R.string.iv_display_desc, null)
        }

    private fun handleResultXO(res: GameResult) = binding.apply {
        tvGameDisplay.text = resources.getString(R.string.winner_message)
        ivGameDisplay.setImageDrawable(if (res == GameResult.X) xDrawable else oDrawable)
        ivGameDisplay.contentDescription = resources.getString(R.string.iv_display_desc, res)
    }

    private fun defineAlertMessage(alert: Alert): String {
        return when(alert) {
            Alert.SomeError -> resources.getString(R.string.internal_error_message)
            Alert.CellOccupied -> resources.getString(R.string.entity_occupied_message, resources.getString(R.string.cell_var))
            Alert.GameFinished -> resources.getString(R.string.game_finished_message)
            Alert.BlockFinished -> resources.getString(R.string.entity_occupied_message, resources.getString(R.string.block_var))
            Alert.BlockInactive -> resources.getString(R.string.block_inactive_message)
        }
    }

    private fun handleCurrBlockNull() {
        if (viewModel.gameMode == ComplexGameMode.Basic)
            return
        else
            paintAllAvailableBlocks()
    }

    private fun handlePrevBlock() {
        if (prevBlockCords == null)
            paintAllBlocks(androidx.appcompat.R.attr.colorPrimary)
        else
            findBlock(prevBlockCords!!).setBlockColor(androidx.appcompat.R.attr.colorPrimary)
    }

    //Initiates observers for viewModel
    private fun initObservers() {
        viewLifecycleOwner.also { owner ->
            viewModel.apply {
                lastTurn.observe(owner) {
                    if (it == null) { // if null, the game have been restarted, rerender empty field
                        field.value?.let { field -> renderAll(field as ComplexFieldModel) }
                        return@observe
                    }
                    handleLastTurn(it)
                }

                currentPlayer.observe(owner) {
                    binding.ivGameDisplay.apply {
                        setImageDrawable(definePlayerDrawable(it))
                        contentDescription = resources.getString(R.string.iv_display_desc, it)
                    }
                }

                gameResult.observe(owner) {
                    when (it) {
                        null -> {
                            handleResultNull(currentPlayer.value)
                            return@observe
                        }
                        GameResult.N -> handleResultN()
                        else -> handleResultXO(it)
                    }
                    paintAllBlocks(androidx.transition.R.attr.colorPrimary)
                }

                alert.observe(owner) {
                    it?.let { alert ->
                        Snackbar.make(
                            binding.root,
                            defineAlertMessage(alert),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    viewModel.clearAlert()
                }

                winLineCode.observe(owner) {
                    when (it) {
                        0 -> return@observe
                        1 -> renderLine(offset = -0.35f)
                        2 -> renderLine()
                        3 -> renderLine(offset = 0.35f)
                        4 -> renderLine(angle = 45f)
                        5 -> renderLine(angle = -45f)
                        6 -> renderLine(offset = 0.35f, angle = 90f)
                        7 -> renderLine(angle = 90f)
                        8 -> renderLine(offset = -0.35f, angle = 90f)
                    }
                }

                currentBlockCords.observe(viewLifecycleOwner) {
                    Log.d(UI_TAG, "Update")
                    if (it == null)
                        handleCurrBlockNull()
                    else {
                        handlePrevBlock()
                        findBlock(it).setBlockColor(androidx.appcompat.R.attr.colorAccent)
                    }
                    prevBlockCords = it
                }
            }
        }
    }

    private fun paintAllBlocks(attrId: Int) {
        for (x in 0..2)
            for (y in 0..2)
                findBlock(ClassicCoordinatesModel(x, y)).setBlockColor(attrId)
    }
    private fun paintAllAvailableBlocks() {
        for (x in 0..2)
            for (y in 0..2) {
                val cords = ClassicCoordinatesModel(x, y)
                if (viewModel.field.value?.get(cords)?.state == null)
                    findBlock(cords).setBlockColor(androidx.constraintlayout.widget.R.attr.colorAccent)
            }
    }

    //Set color from theme by R.attr integer
    private fun ConstraintLayout.setBlockColor(attrId: Int) {
        val tv = TypedValue()
        context?.theme?.resolveAttribute(attrId, tv, true)

        for (i in this.children) {
            if (i is ImageButton)
                continue
            i.setBackgroundColor(tv.data)
        }
    }

    //Finds ImageButton cell by cords
    private fun findCell(block: ConstraintLayout, cords: ClassicCoordinatesModel): ImageButton {
        return getIdByString("ibCell${cords.x}${cords.y}").let {
            block.findViewById(it)
        }
    }

    //Fills blockButtons with ImageButtons
    private fun initBlockButtons() {
        for (i in 0..8) {
            val x = i / 3
            val y = i % 3

            val block = findBlock(ClassicCoordinatesModel(x, y))

            for (j in 0..8) {
                val x1 = j / 3
                val y1 = j % 3

                blockButtons[i][x1][y1] = findCell(block, ClassicCoordinatesModel(x1, y1))
            }
        }
    }

    //Listener for making turn (For cells)
    private fun makeTurnListener(cords: ComplexCoordinatesModel): View.OnClickListener {
        return View.OnClickListener {
            viewModel.makeTurn(cords)
        }
    }

    //Returns Int id by its string name
    private fun getIdByString(name: String): Int {
        return resources.getIdentifier(name, "id", context?.packageName)
    }

    //Returns ImageButton by coordinates
    private fun getImageButton(cords: ComplexCoordinatesModel) =
        blockButtons[cords.x*3 + cords.y][cords.innerCoordinates.x][cords.innerCoordinates.y]
}