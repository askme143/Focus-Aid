package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.databinding.DialogNotiHandlingBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.NotificationActionViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.NotificationActionViewModelFactory

class NotiHandlingDialog : BottomSheetDialogFragment() {
    private var _binding: DialogNotiHandlingBinding? = null
    private val binding get() = _binding!!

    private val directions = NotiHandlingDialogDirections

    private val notiActionViewModel by activityViewModels<NotificationActionViewModel> {
        NotificationActionViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogNotiHandlingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = notiActionViewModel

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotiHandlingDialogToNotificationActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            notiActionViewModel.editing = true
            findNavController().navigateSafe(directions.actionNotiHandlingDialogToNotificationActionFragment())
        }

        return binding.root
    }
}