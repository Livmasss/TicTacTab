package com.livmas.tictactab.ui.fragments.game.sessions.classic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.livmas.tictactab.GAME_TAG
import com.livmas.tictactab.R
import com.livmas.tictactab.databinding.FragmentClassicGameSessionBinding
import com.livmas.tictactab.domain.models.ICoordinatesModel
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel
import com.livmas.tictactab.domain.models.enums.GameResult
import com.livmas.tictactab.ui.fragments.game.sessions.GameSessionFragment
import com.livmas.tictactab.ui.models.enums.Alert

class ClassicGameSessionFragment : GameSessionFragment() {

    override val viewModel: ClassicGameSessionViewModel by activityViewModels()
    private lateinit var binding: FragmentClassicGameSessionBinding
    private lateinit var imageMatrix: Array<Array<ImageButton>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassicGameSessionBinding.inflate(LayoutInflater.from(context), container, false)

        binding.field.apply {
            imageMatrix = arrayOf(
                arrayOf(ibCell00, ibCell01, ibCell02),
                arrayOf(ibCell10, ibCell11, ibCell12),
                arrayOf(ibCell20, ibCell21, ibCell22)
            )
        }
        fieldContainer = binding.flFieldContainer

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        viewModel.field.value?.let { renderField(it, imageMatrix) }
        initObservers()
        viewModel.resumeGame()
    }

    override fun renderCell(imageButton: ImageButton, cords: ICoordinatesModel) {
        viewModel.field.value?.get(cords as ClassicCoordinatesModel)?.let {
            renderCell(imageButton, it)
        }
    }


    private fun initViews() {
        initCells()

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
            lastTurn.observe(viewLifecycleOwner) {
                if (it == null)
                    viewModel.field.value?.let { field -> renderField(field, imageMatrix) }
                it?.let {
                    renderCell(imageMatrix[it.x][it.y], it as ClassicCoordinatesModel)
                }
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
                            Alert.CellOccupied -> resources.getString(R.string.entity_occupied_message, resources.getString(R.string.cell_var))
                            Alert.GameFinished -> resources.getString(R.string.game_finished_message)
                            else -> resources.getString(R.string.internal_error_message)
                        },
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                viewModel.clearAlert()
            }
            winLineCode.observe(viewLifecycleOwner) {
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
    private fun makeTurnListener(cords: ClassicCoordinatesModel): OnClickListener {
        return OnClickListener {
            viewModel.makeTurn(cords)
        }
    }
}