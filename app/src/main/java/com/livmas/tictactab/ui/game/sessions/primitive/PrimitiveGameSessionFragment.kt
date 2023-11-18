package com.livmas.tictactab.ui.game.sessions.primitive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.livmas.tictactab.databinding.FragmentPrimitiveGameSessionBinding

class PrimitiveGameSessionFragment : Fragment() {

    companion object {
        fun newInstance() = PrimitiveGameSessionFragment()
    }

    private val viewModel: PrimitiveGameSessionViewModel by activityViewModels()
    private lateinit var binding: FragmentPrimitiveGameSessionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrimitiveGameSessionBinding.inflate(inflater, container, false)
        return binding.root
    }
}