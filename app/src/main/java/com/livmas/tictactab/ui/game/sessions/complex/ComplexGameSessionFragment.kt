package com.livmas.tictactab.ui.game.sessions.complex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.livmas.tictactab.databinding.FragmentComplexGameSessionBinding

class ComplexGameSessionFragment : Fragment() {

    companion object {
        fun newInstance() = ComplexGameSessionFragment()
    }

    private val viewModel: ComplexGameSessionViewModel by activityViewModels()
    private lateinit var binding: FragmentComplexGameSessionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComplexGameSessionBinding.inflate(inflater, container, false)
        return binding.root
    }
}