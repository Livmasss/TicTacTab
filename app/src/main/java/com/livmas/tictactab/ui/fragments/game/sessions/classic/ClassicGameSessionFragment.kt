package com.livmas.tictactab.ui.fragments.game.sessions.classic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.livmas.tictactab.databinding.FragmentClassicGameSessionBinding
import com.livmas.tictactab.domain.models.classic.ClassicCoordinatesModel

class ClassicGameSessionFragment : Fragment() {

    companion object {
        fun newInstance() = ClassicGameSessionFragment()
        const val logTag = "Game field"
    }

    private val viewModel: ClassicGameSessionViewModel by activityViewModels()
    private lateinit var binding: FragmentClassicGameSessionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassicGameSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCells()
        initObservers()
        viewModel.startGame()
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
        viewModel.field.observe(viewLifecycleOwner) {
            for (x in 0..2)
                for (y in 0..2) {
                    Log.i(logTag, it[x, y].toString())
                }
        }
    }
    private fun makeTurnListener(cords: ClassicCoordinatesModel): OnClickListener {
        return OnClickListener {
            viewModel.makeTurn(cords)
        }
    }
}