package com.yeongil.focusaid.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.focusaid.R
import com.yeongil.focusaid.databinding.DialogAppBlockEntryBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.action.AppBlockActionViewModel
import com.yeongil.focusaid.viewModel.viewModel.action.AppBlockEntryViewModel
import com.yeongil.focusaid.viewModelFactory.AppBlockActionViewModelFactory


class AppBlockEntryDialog : BottomSheetDialogFragment() {
    private var _binding: DialogAppBlockEntryBinding? = null
    private val binding get() = _binding!!

    private val directions = AppBlockEntryDialogDirections

    private val appBlockActionViewModel by activityViewModels<AppBlockActionViewModel> {
        AppBlockActionViewModelFactory(requireContext())
    }
    private val appBlockEntryViewModel by activityViewModels<AppBlockEntryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAppBlockEntryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = appBlockEntryViewModel

        binding.timePicker.setIs24HourView(true)
        binding.doNotAllowBtn.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.purple_background)
        )

        disableTimePicker()

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockEntryDialogToAppBlockActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            val entry = appBlockEntryViewModel.getAppBlockEntry()
            appBlockActionViewModel.updateAppBlockEntryList(entry)
            findNavController().navigateSafe(directions.actionAppBlockEntryDialogToAppBlockActionFragment())
        }

        return binding.root
    }

    private fun disableTimePicker() {
        fun disable(viewGroup: ViewGroup) {
            for (i in 0 until viewGroup.childCount) {
                val view = viewGroup.getChildAt(i)

                if (view is ViewGroup) disable(view)
                else view.isEnabled = false
            }
        }

        disable(binding.timePicker)
    }
}