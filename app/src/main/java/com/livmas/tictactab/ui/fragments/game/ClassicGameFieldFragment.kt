package com.livmas.tictactab.ui.fragments.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.livmas.tictactab.databinding.FragmentClassicGameFieldBinding

class ClassicGameFieldFragment : Fragment() {

    private lateinit var viewModel: ClassicGameFieldViewModel
    private lateinit var _binding: FragmentClassicGameFieldBinding
    val binding: FragmentClassicGameFieldBinding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassicGameFieldBinding.inflate(layoutInflater, container, false)
        return _binding.root
    }
}