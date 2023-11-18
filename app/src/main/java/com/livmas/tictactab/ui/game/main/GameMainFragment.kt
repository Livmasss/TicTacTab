package com.livmas.tictactab.ui.game.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.livmas.tictactab.databinding.FragmentGameMainBinding

class GameMainFragment : Fragment() {

    companion object {
        fun newInstance() = GameMainFragment()
    }

    private val viewModel: GameMainViewModel by activityViewModels()
    private lateinit var binding: FragmentGameMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameMainBinding.inflate(inflater, container, false)
        return binding.root
    }
}