package com.livmas.tictactab.ui.fragments.game.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.livmas.tictactab.R
import com.livmas.tictactab.data.repositories.SettingsRepository
import com.livmas.tictactab.databinding.FragmentGameMainBinding
import com.livmas.tictactab.ui.ResourceExtractor
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class GameMainFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentGameMainBinding
    private lateinit var extractor: ResourceExtractor

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
        extractor = ResourceExtractor(resources, requireContext())
        initListeners()
        initSpinner()
    }

    override fun onStop() {
        val mode = ComplexGameMode.values()[binding.spinGameModeSelector.selectedItemPosition]
        SettingsRepository.instance.putCompGameMode( mode )

        super.onStop()
    }

    private fun initListeners() {
        binding.bStartClassic.setOnClickListener {
            navController.navigate(R.id.action_gameMainFragment_to_navigation_game_primitive_session)
        }
        binding.bStartComplex.setOnClickListener {
            navController.navigate(R.id.action_gameMainFragment_to_navigation_game_complex_session)
        }
    }

    private fun initSpinner() {
        val arrayList = ArrayList<String>()
        for (i in ComplexGameMode.values()) {
            arrayList.add(extractor.getStringByName("complex_mode_" + i.value.toString()))
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, arrayList)
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.select_dialog_item_material)
        binding.spinGameModeSelector.adapter = adapter
    }
}