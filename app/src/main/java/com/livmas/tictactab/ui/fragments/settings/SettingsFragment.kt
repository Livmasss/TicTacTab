package com.livmas.tictactab.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.livmas.tictactab.databinding.FragmentSettingsBinding
import com.livmas.tictactab.ui.ThemeManager

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val state = SettingsState()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    override fun onStop() {
        state.loadData()
        initViews()
        super.onStop()
    }

    private fun initViews() {
        binding.sNightMode.isChecked = state.nightTheme == true
    }

    private fun initListeners() {
        initNightModeListener()
//        initConfirmButtonListener()
    }

    private fun initNightModeListener() {
        binding.sNightMode.setOnCheckedChangeListener { _, b ->
            state.nightTheme = b

            state.saveNightMode()
            ThemeManager.setTheme(binding.sNightMode.isChecked)
        }
    }

//    private fun initConfirmButtonListener() {
//        binding.bConfirm.setOnClickListener {
//            SettingsRepository.instance.apply {
//                val repStates = SettingsState(
//                    readCompGameMode(),
//                    readNightMode())
//                Log.d(SETTINGS_TAG, (state == repStates).toString())
//
//                if (state == repStates)
//                    return@setOnClickListener
//
//                confirm()
//            }
//        }
//    }
//
//    private fun confirm() {
//        state.saveNightMode()
//        ThemeManager.setTheme(binding.sNightMode.isChecked)
//    }
}