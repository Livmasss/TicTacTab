package com.livmas.tictactab.ui.fragments.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.livmas.tictactab.databinding.FragmentSettingsBinding
import com.livmas.tictactab.ui.ThemeManager
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val states = SettingsStates()

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
        states.loadData()
        initViews()
        super.onStop()
    }

    private fun initViews() {
        states.apply{
            val idToCheck = binding.rbgComplexGameMode[complexGameMode.value].id
            binding.rbgComplexGameMode.check(idToCheck)

            binding.sNightMode.isChecked = nightTheme == true
            binding.cbUseNight.isChecked = useNightTheme == true
        }
    }

    private fun initListeners() {
        initNightSListener()
        initUseNightCBListener()
        initConfirmButtonListener()
        initRBGListener()
    }

    private fun initUseNightCBListener() {
        binding.cbUseNight.setOnCheckedChangeListener { _, b ->
            ThemeManager.useTheme = b
            states.useNightTheme = b
        }
    }

    private fun initNightSListener() {
        binding.sNightMode.setOnCheckedChangeListener { _, b ->
            states.nightTheme = b
        }
    }

    private fun initRBGListener() {
        binding.rbgComplexGameMode.setOnCheckedChangeListener { group, id ->
            val index = group.indexOfChild(group.findViewById(id))
            states.complexGameMode = ComplexGameMode.values()[index]
        }
    }

    private fun initConfirmButtonListener() {
        binding.bConfirm.setOnClickListener {
            showConfirmAlert()
        }
    }

    private fun showConfirmAlert() {
        AlertDialog.Builder(context)
            .setNegativeButton("cancel") { _, _ -> }
            .setPositiveButton("confirm") {_, _ -> confirm()}
            .setMessage("If you change game mode, game will be restarted")
            .setTitle("Are you sure?")
            .show()
    }

    private fun confirm() {
        states.saveData()
        ThemeManager.useTheme = binding.cbUseNight.isChecked
        ThemeManager.setTheme(binding.sNightMode.isChecked)
    }
}