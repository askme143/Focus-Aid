package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.FragmentActionEditBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppBlockActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.NotificationActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.AppBlockActionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.NotificationActionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class ActionEditFragment : Fragment() {
    private var _binding: FragmentActionEditBinding? = null
    private val binding get() = _binding!!

    private val directions = ActionEditFragmentDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val appBlockActionViewModel by activityViewModels<AppBlockActionViewModel> {
        AppBlockActionViewModelFactory(requireContext())
    }
    private val notificationViewModel by activityViewModels<NotificationActionViewModel> {
        NotificationActionViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionEditBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener { findNavController().navigateSafe(directions.actionGlobalActionFragment()) }

        binding.appTimeBtn.setOnClickListener {
            appBlockActionViewModel.putAppBlockAction(ruleEditViewModel.editingRule.value!!.appBlockAction)
            findNavController().navigateSafe(directions.actionActionEditFragmentToAppBlockActionFragment())
        }
        binding.notificationBtn.setOnClickListener {
            notificationViewModel.putNotificationAction(ruleEditViewModel.editingRule.value!!.notificationAction)
            findNavController().navigateSafe(directions.actionActionEditFragmentToNotificationActionFragment())
        }
        binding.dndBtn.setOnClickListener { findNavController().navigateSafe(directions.actionActionEditFragmentToDndDialog()) }
        binding.ringerBtn.setOnClickListener { findNavController().navigateSafe(directions.actionActionEditFragmentToRingerDialog()) }

        return binding.root
    }
}