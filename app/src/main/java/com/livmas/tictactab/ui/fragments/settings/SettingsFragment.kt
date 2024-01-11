package com.livmas.tictactab.ui.fragments.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.livmas.tictactab.databinding.FragmentSettingsBinding
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by activityViewModels()
    companion object {
        const val TAG = "settings"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showValues()
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.apply{
            complexGameMode.observe(viewLifecycleOwner) {
                val idToCheck = binding.rbgComplexGameMode[it.value].id
                binding.rbgComplexGameMode.check(idToCheck)
            }

            binding.sNightMode.isChecked = nightTheme.value == true
        }
    }

    private fun showValues() {
        viewModel.complexGameMode.value?.apply {
            binding.rbgComplexGameMode.check(value)
        }
    }

    private fun initListeners() {
        initRGBListener()
        initNightSwitchListener()
        initConfirmButtonListener()
    }

    private fun initRGBListener() {
        binding.rbgComplexGameMode.setOnCheckedChangeListener { group, i ->
            val rb = group.findViewById<RadioButton>(i)
            val gameMode = ComplexGameMode.values()[group.indexOfChild(rb)]
            Log.i(TAG, gameMode.toString())

            viewModel.postGameMode(gameMode)
        }
    }

    private fun initNightSwitchListener() {
        binding.sNightMode.setOnCheckedChangeListener { _, b ->
            viewModel.postNightMode(b)
        }
    }

    private fun initConfirmButtonListener() {
        binding.apply{
            bConfirm.setOnClickListener {
                viewModel.postNightMode(sNightMode.isChecked)

                viewModel.saveData()
            }
        }
    }
}