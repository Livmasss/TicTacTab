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
import com.livmas.tictactab.SETTINGS_TAG
import com.livmas.tictactab.databinding.FragmentSettingsBinding
import com.livmas.tictactab.ui.ThemeManager
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.apply{
            complexGameMode.observe(viewLifecycleOwner) {
                val idToCheck = binding.rbgComplexGameMode[it.value].id
                binding.rbgComplexGameMode.check(idToCheck)
                Log.i(SETTINGS_TAG, "Game mode observed: $it")
            }

            nightTheme.observe(viewLifecycleOwner) {
                binding.sNightMode.isChecked = it
            }
        }
    }

    private fun initListeners() {
        initNightSwitchListener()
        initNightCBListener()
        initConfirmButtonListener()
    }
    
    private fun initNightSwitchListener() {
        binding.sNightMode.setOnCheckedChangeListener { _, b ->
            ThemeManager.setTheme(b)
        }
    }

    private fun initNightCBListener() {
        binding.cbUseNight.setOnCheckedChangeListener { _, b ->
            viewModel.postUseNightTheme(b)
            ThemeManager.useTheme = b
        }
    }

    private fun initConfirmButtonListener() {
        binding.bConfirm.setOnClickListener {
            binding.rbgComplexGameMode.apply {
                val rb = findViewById<RadioButton>(checkedRadioButtonId)
                val gameMode = ComplexGameMode.values()[indexOfChild(rb)]

                Log.d(SETTINGS_TAG, "Confirm")
                viewModel.postGameMode(gameMode)
            }

            viewModel.postNightTheme(binding.sNightMode.isChecked)
        }
    }
}