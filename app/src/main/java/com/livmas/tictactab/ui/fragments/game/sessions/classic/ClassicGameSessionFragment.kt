package com.livmas.tictactab.ui.fragments.game.sessions.classic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.livmas.tictactab.databinding.FragmentClassicGameSessionBinding

class ClassicGameSessionFragment : Fragment() {

    companion object {
        fun newInstance() = ClassicGameSessionFragment()
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
}