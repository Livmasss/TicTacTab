package com.livmas.tictactab.ui.fragments.game.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.livmas.tictactab.R
import com.livmas.tictactab.databinding.FragmentGameMainBinding

class GameMainFragment : Fragment() {

    companion object {
        fun newInstance() = GameMainFragment()
    }

    private val viewModel: GameMainViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentGameMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.bStartClassic.setOnClickListener {
            navController.navigate(R.id.action_gameMainFragment_to_navigation_game_primitive_session)
        }
        binding.bStartComplex.setOnClickListener {
            navController.navigate(R.id.action_gameMainFragment_to_navigation_game_complex_session)
        }
    }

    
}