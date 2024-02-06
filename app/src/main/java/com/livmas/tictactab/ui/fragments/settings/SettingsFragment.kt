package com.livmas.tictactab.ui.fragments.settings

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.livmas.tictactab.R
import com.livmas.tictactab.SETTINGS_TAG
import com.livmas.tictactab.data.repositories.SettingsRepository
import com.livmas.tictactab.databinding.FragmentSettingsBinding
import com.livmas.tictactab.ui.ThemeManager
import com.livmas.tictactab.ui.models.enums.ComplexGameMode

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
        state.apply{
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
            state.useNightTheme = b
        }
    }

    private fun initNightSListener() {
        binding.sNightMode.setOnCheckedChangeListener { _, b ->
            state.nightTheme = b
        }
    }

    private fun initRBGListener() {
        binding.rbgComplexGameMode.setOnCheckedChangeListener { group, id ->
            val index = group.indexOfChild(group.findViewById(id))
            state.complexGameMode = ComplexGameMode.values()[index]
        }
    }

    private fun initConfirmButtonListener() {
        binding.bConfirm.setOnClickListener {
            SettingsRepository.instance.apply {
                val repStates = SettingsState(
                    readCompGameMode(),
                    readNightMode(),
                    readUseNightMode())
                Log.d(SETTINGS_TAG, (state == repStates).toString())

                if (state == repStates)
                    return@setOnClickListener

                showConfirmAlert()
            }
        }
    }

    private fun showConfirmAlert() {
        AlertDialog.Builder(context)
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.confirm) {_, _ -> confirm()}
            .setMessage(if (SettingsRepository.instance.readCompGameMode() != binding.rbgComplexGameMode.getCheckedMode())
                resources.getString(R.string.mode_changed_confirm_message)
            else
                "")
            .setTitle(R.string.confirm_title_message)
            .show()
    }

    private fun confirm() {
        state.saveData()
        ThemeManager.useTheme = binding.cbUseNight.isChecked
        ThemeManager.setTheme(binding.sNightMode.isChecked)
    }

    private fun RadioGroup.getCheckedMode(): ComplexGameMode {
        val index = indexOfChild(findViewById(checkedRadioButtonId))
        return ComplexGameMode.values()[index]
    }
}